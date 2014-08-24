/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.visualizationunits;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.sql.SQLException;
import probe.com.handlers.AuthenticatorHandler;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.User;
import probe.com.view.subview.util.Help;

/**
 *
 * @author Yehia Farag
 */
public class AdminLayout extends VerticalLayout implements Serializable, Button.ClickListener {
    
    private VerticalLayout loginLayout = new VerticalLayout();
    private VerticalLayout controlPanelLayout = new VerticalLayout();
    private VerticalLayout errorLayout = new VerticalLayout();
    private MainHandler handler;
    private User user =new User();
    private AuthenticatorHandler auth;
    private Button signOutButton;
    private VerticalLayout helpLayout;
    private Help help = new Help();//help notes
    private HorizontalLayout helpNote;
    private  TextField usernameField;
    private PasswordField passwordField ;
    private AdminControlPanel controlPanel;

    public AdminLayout(MainHandler handler)
    {
        this.handler = handler;
        this.auth = handler.getAuthenticator();
        this.setWidth("100%");
        this.addComponent(loginLayout);
        this.addComponent(controlPanelLayout);
        initLoginLayout();
        controlPanelLayout.setVisible(false);
         
        signOutButton = new Button("Sign Out");
        signOutButton.setStyleName(BaseTheme.BUTTON_LINK);
        signOutButton.setWidth("10%");
        controlPanelLayout.addComponent(signOutButton);
        controlPanelLayout.setComponentAlignment(signOutButton, Alignment.TOP_RIGHT);
        signOutButton.addListener(new Button.ClickListener() {

            /**
             *
             */
            private static final long serialVersionUID = 1L;
            @Override
            public void buttonClick(Button.ClickEvent event) {
                user = null;
                controlPanelLayout.setVisible(false);
                loginLayout.setVisible(true);
                

            }
        });

       
    }
    private void initLoginLayout()
    {        
        loginLayout.removeAllComponents();
        Panel loginPanel = new Panel("Sign In");
        loginPanel.setStyleName(Reindeer.PANEL_LIGHT);
        loginPanel.setHeight("250px");
        VerticalLayout loginPanelLayout = new VerticalLayout();        
    
        VerticalLayout spacer1= new VerticalLayout();
        VerticalLayout spacer2= new VerticalLayout();
        VerticalLayout spacer3= new VerticalLayout();
        spacer1.setHeight("10px");
        spacer2.setHeight("10px");
        spacer3.setHeight("20px");
        
//        loginPanelLayout.setHeight("250px");
        loginPanelLayout.addComponent(spacer1);
        
        usernameField = new TextField("Email:");
        usernameField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        usernameField.setRequired(true);       
        usernameField.setRequiredError("Please Enter a Valid Username! ");
        loginPanelLayout.addComponent(usernameField);
        
        loginPanelLayout.addComponent(spacer2);
         
        passwordField = new PasswordField("Password:");
        passwordField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        passwordField.setRequired(true);
        passwordField.setRequiredError("Please Enter a Valid Password! ");      
        loginPanelLayout.addComponent(passwordField);       
        
        loginPanelLayout.addComponent(spacer3);

        final Button signInButton = new Button("Sign In");
        signInButton.setStyleName(Reindeer.BUTTON_SMALL);
        loginPanelLayout.addComponent(signInButton);
        usernameField.addListener(new FieldEvents.TextChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                // TODO Auto-generated method stub
                signInButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
            }
        });
        
        
        Label l = new Label("<h5>Login To Be Able To Add/Edit Experiments </h5><h5 style='color:blue'>For Sign Up Please Contact Us at admin@csf.no </h5>");
        l.setContentMode(Label.CONTENT_XHTML);     
        helpLayout = new VerticalLayout();
        helpLayout.setWidth("100%");
        helpNote = help.getHelpNote(l);

        loginPanelLayout.addComponent(helpLayout);
        helpLayout.addComponent(helpNote);
        helpLayout.setComponentAlignment(helpNote, Alignment.MIDDLE_RIGHT);        
        loginPanelLayout.addComponent(errorLayout);
        loginPanelLayout.setComponentAlignment(errorLayout,Alignment.TOP_LEFT );
        errorLayout.setHeight("50px");
        errorLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        loginPanel.setContent(loginPanelLayout);
        loginLayout.addComponent(loginPanel);
        signInButton.addListener(this);   
    
    }
    @Override
    public void buttonClick(Button.ClickEvent event) {//on click login button
        errorLayout.removeAllComponents();
        String username = (String) usernameField.getValue();
        String password = (String) passwordField.getValue();
        boolean valid = false;//validate login fields
        if (username == null || username.equals("")) {
        } else if ((password == null || password.equals(""))) {
        } else {
            try {
                user = auth.authenticate(username.toUpperCase(), password);//Authenticate username and password
                if (user != null) {
                    valid = true;
                }

            } catch (SQLException e) {
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            } catch (ClassNotFoundException e) {
            }
        }
        if (valid == false) {	//user not valid 

            Label error = new Label("<h4 style='color:red'>Please Check Your Registered Email or Password ! </h4>");
            error.setContentMode(Label.CONTENT_XHTML);
            errorLayout.addComponent(error);
        } else //user name and password are OK
        {
            usernameField.setValue("");
            passwordField.setValue("");
            loginLayout.setVisible(false);
            initControlPanelLayout();

        }
    }
    
    private void initControlPanelLayout()
    {
        
        if(controlPanel !=null)
            controlPanelLayout.removeComponent(controlPanel);
        controlPanel = new AdminControlPanel(user, handler);
        controlPanelLayout.addComponent(controlPanel);
        controlPanelLayout.setVisible(true); 
    
    }

    
}
