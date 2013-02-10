
package net.ikhlasstudio.kmitlwifi.login;

import net.ikhlasstudio.kmitwifi.util.LoginResult;

public interface Loginable {

    public LoginResult login();

    public LoginResult logout();
}
