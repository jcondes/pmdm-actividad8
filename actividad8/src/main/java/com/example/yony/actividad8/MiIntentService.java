package com.example.yony.actividad8;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import gebulot.pmdmlib.PushNotificationAdmin.Consts;
import gebulot.pmdmlib.PushNotificationAdmin.GcmBroadcastReceiver;

/**
 * Created by Yony on 07/02/16.
 */
public class MiIntentService extends IntentService {

    public static final String GCM_INTENT_SERVICE = "GcmIntentService";
    public static final String GCM_SEND_ERROR = "Send error: ";
    public static final String GCM_DELETED_MESSAGE = "Deleted messages on server: ";
    public static final String GCM_RECEIVED = "Received: ";
    public static final String EXTRA_MESSAGE = "message";

    public static final int NOTIFICATION_ID = 1;

    private static final String TAG = MiIntentService.class.getSimpleName();

    private NotificationManager notificationManager;

    public MiIntentService(){
        super(GCM_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "new push");

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = googleCloudMessaging.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                processNotification(GCM_SEND_ERROR, extras);
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                processNotification(GCM_DELETED_MESSAGE, extras);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                processNotification(GCM_RECEIVED, extras);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void processNotification(String type, Bundle extras) {
        //GESTOR DE NOTIFICACIONES DEL MOVIL ANDROID OS.
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //

        final String messageValue = extras.getString(EXTRA_MESSAGE);//SACAMOS EL TEXTO DEL MENSAJE RECIBIDO.
        final String idUs = extras.getString("idUsuario");
        final String nameUs = extras.getString("nombreUsuario");
        Log.v("MiIntentService","########################" + messageValue + " " + idUs);
        final String qbContentID=extras.getString("rich.content");//EN CASO DE ENVIAR UN ID DE CONTENIDO IMAGEN DE QB

        Intent intent = new Intent(this, MessagesActivity.class);//DEFINIMOS LA ACTIVIDAD QUE SE EJECUTARA CUANDO SE PINCHE SOBRE
                                                                //EL MENSAJE PUSH. COMO VARIABLE DE ENTRADA DEL INTENT, LE PASAREMOS
                                                                //EL TEXTO DEL MENSAJE PUSH. RECORDAR QUE ES POR AQUI DONDE
                                                                //SE PUEDE PASAR MAS INFORMACION (URLS PARA DESCARGA, ETC.).
        intent.putExtra(EXTRA_MESSAGE, messageValue);
        intent.putExtra("idUsuario", idUs);
        intent.putExtra("nombreUsuario", nameUs);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);//SE DEFINE QUE EL INTENT DE ARRIBA ES UN
                                                                                    //INTENT EN DIFERIDO (PENDING)

        //CONSTRUIMOS EL MENSAJE DE PUSH NOTIFICATIONS QUE APARECERA EN LA PARTE SUPERIOR DEL MOVIL
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_utad)//ICONO QUE APARECERA AL LADO DEL PUSH NOTIFICATION
                        .setContentTitle("TITULO MENSAJE")//TITULO QUE APARECERA EN EL PUSH NOTIFICATION.
                        .setStyle(new NotificationCompat.BigTextStyle()//ESTILO DE LA FUENTE DEL TEXTO DEL MENSAJE QUE SE MOSTRARA
                                .bigText(messageValue))
                        .setContentText(messageValue);//EL TEXTO DEL MENSAJE

        mBuilder.setContentIntent(contentIntent);//LE PASAMOS EL INTENT QUE SE EJECUTARA CUANDO SE PINCHE SOBRE SOBRE EL
                                                //PUSH NOTIFICATIONS. CUANDO SE PINCHA SOBRE EL MENSAJE SE SUELE ENTRAR
                                                //DEVUELTA A LA APP. PARA ELLO, SE DEBE PROPORCIONAR EL INTENT(ACTIVITY) QUE
                                                //SE EJECUTARA CUANDO SE PINCHE SOBRE EL MENSAJE.

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());//ESTE METODO ES EL QUE PUBLICA EN LA PARTE SUPERIOR
                                                                        //DEL MOVIL EL MENSAJE. NOTIFICATIONMANAGER ES EL
                                                                    //OBJETO QUE REPRESENTA EL GESTOR DE NOTIFICATIONES. EL
                                                                    //ID ES UN CODIGO QUE SE PUEDE USAR PARA DIFERENCIAR ENTRE
                                                                    //LOS PUSH NOTIFICATIONS.

        // notify activity
        //El intentNewPush es el mPushReceiver definido en el DataHolder. Cuando llega un mensaje, el ultimo paso es
        //ejecutar el proceso que hayamos definido en el mPushReceiver. Por ejemplo, si queremos guardar los mensajes que
        //llegan en una lista (ArrayList), o guardarlo en un fichero local, etc., pues se ejecuta desde el mPushReceiver.
        //Para poder ejecutar el mismo proceso que definido en el DataHolder, se debe usar el mismo nombre de intent.
        //Consts.NEW_PUSH_EVENT es el nombre.
        Intent intentNewPush = new Intent(Consts.NEW_PUSH_EVENT);
        intentNewPush.putExtra(EXTRA_MESSAGE, messageValue);
        intentNewPush.putExtra("idUsuario", idUs);
        intentNewPush.putExtra("nombreUsuario", nameUs);
        if(qbContentID!=null)intentNewPush.putExtra("QBCID", messageValue);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentNewPush);

        //Log.i(TAG, "Broadcasting event " + "PUSH_EVENT" + " with data: " + messageValue);

    }
}
