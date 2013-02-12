
package net.ikhlasstudio.kmitlwifi.login;

import net.ikhlasstudio.kmitlwifi.util.LoginResult;

public interface Loginable {
    public static int DO_LOGIN = 1;
    public static int DO_LOGOUT = 2;

    public LoginResult login();

    public LoginResult logout();
    
    public boolean testInternetAccess();
}
