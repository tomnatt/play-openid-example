package controllers;

import play.*;
import play.mvc.*;
import play.libs.OpenID;
import play.libs.OpenID.UserInfo;

import java.util.*;

import models.*;

public class Application extends Controller {

    @Before(unless={"login", "authenticate"})
    static void checkAuthenticated() {

        System.out.println("checkAuthenticated");
        
        if (!session.contains("user")) {
            login();
        }
        
    }
     
    public static void index() {
        String username = session.get("user");
        render(username);
    }
         
    public static void login() {
        System.out.println("login");
        render();
    }
        
    public static void authenticate(String user) {

        System.out.println("authenticate");
        System.out.println("user: " + user);

        if (OpenID.isAuthenticationResponse()) {
            
            UserInfo verifiedUser = OpenID.getVerifiedID();

            System.out.println("verifiedUser: " + verifiedUser.id);
            
            if (verifiedUser == null) {
                flash.put("error", "Oops. Authentication has failed");
                login();
            } 
            session.put("user", verifiedUser.id);
            index();
            
        } else {

            System.out.println("about to verify");
            OpenID.id(user).verify(); // will redirect the user
            System.out.println("verified");
            
        }
        
    }

}
