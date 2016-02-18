package gebulot.pmdmlib.QbAdmin;

import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

/**
 * Created by Yony on 20/01/2016.
 */
public interface QbAdminListener {

    public void sessionCreated(boolean blCreated);
    public void loginSuccess(boolean blLogin, int id, String name);
    public void registerSuccess(boolean blLogin);
    public void getTableSuccess(long timeID,ArrayList<QBCustomObject> customObjects);
    public void getUsersSuccess(boolean blRetrieved, ArrayList<QBUser> qbUsers);
}
