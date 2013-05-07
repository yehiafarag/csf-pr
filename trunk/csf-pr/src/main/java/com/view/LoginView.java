package com.view;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.handlers.Authenticator;
import com.handlers.ExperimentHandler;
import com.helperunits.Help;
import com.model.beans.ExperimentBean;
import com.model.beans.User;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.view.subviewunits.UploadUnit;

public class LoginView extends VerticalLayout implements  TabSheet.SelectedTabChangeListener,Serializable, Button.ClickListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerticalLayout layout1 = new VerticalLayout();
	private VerticalLayout layout2 = new VerticalLayout();
	private VerticalLayout layout3 = new VerticalLayout();
	private TabSheet t;
	private TabSheet mainTab;
	private VerticalLayout removeExperimentLayout;
	@SuppressWarnings("unused")
	private Tab t1, t2,t3;
	private VerticalLayout  l1;
	private VerticalLayout l2,l3;
	private ExperimentHandler eh ;
	private Map<Integer,ExperimentBean> expList;
	
	private Help help = new Help();//help notes
	private HorizontalLayout helpNote;
	
	private  TextField usernameField;
    private  PasswordField  passwordField,oldPasswordField,newPasswordField;
    private  Panel root1;
    private  Panel root2;
    private VerticalLayout changePasswordPanel,helpLayout;
    
    private Button signInButton,changePasswordButton;
    private Button signOutButton;
    private Label error;
    private Authenticator auth;
    private User user;
    private VerticalLayout adminLayout;
    private Label addUserLabel,removeUserLabel;
    private HorizontalSplitPanel hsplit;
    private VerticalLayout leftSide, rightSide;
    private Select select, selectExp;
    private Form removeUserForm ; 
    private Form removeExperimentForm;
    private Form newLoginForm;
	private Map<Integer,String> usersList;
	private String url, dbName, driver, userName,  password;
    
    public LoginView(String url,String dbName,String driver,String userName, String password,TabSheet mainTab) {
		eh = new ExperimentHandler( url,dbName,driver,userName, password);
		auth= new  Authenticator(url,dbName,driver,userName, password);
		this.url = url;
		this.mainTab = mainTab;
		this.dbName = dbName;
		this.driver = driver;
		this.userName = userName;
		this.password = password;
		  t = new TabSheet();
       this.initRoot1();//create and update login view
       this.addComponent(root1);
        
    }

	public void buttonClick(ClickEvent event) {//on click login button
		String username = (String)usernameField.getValue();
		String password = (String)passwordField.getValue();
		boolean valid = false;//validate login fields
		
		if(username == null || username.equals(""))
		{
			
			
		}
		else if((password == null || password.equals("")))
		{
				
		}
		else
		{
			try {
				user = auth.authenticate(username.toUpperCase(),password);//Authenticate username and password
				if(user != null)
					valid = true;
				
			} catch (SQLException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			} catch (ClassNotFoundException e) {
			}
		}
		if(valid == false){	//user not valid 
		
			if(error != null)
				root1.removeComponent(error);
			 error = new Label("<h4 style='color:red'>Please Check Your Registered Email or Password ! </h4>");
			 error.setContentMode(Label.CONTENT_XHTML);
			 root1.addComponent(error);
		}else	//user name and password are OK
		{
			usernameField.setValue("");
			passwordField.setValue("");
			if(error != null)
				root1.removeComponent(error);
			this.removeComponent(root1);
			this.initRoot2();
        	this.addComponent(root2);
			
		}
	}
	@SuppressWarnings("deprecation")
	private void initRoot1()//login form 
	{
		if(signOutButton != null)
			this.removeComponent(signOutButton);
		 root1 = new Panel("Sign In");	
	     root1.setStyle(Reindeer.PANEL_LIGHT);   
	    
	     
	     
	     
	     Label l = new Label("<h5>Login To Be Able To Add/Edit Experiments </h5><h5 style='color:blue'>For Sign Up Please Contact Us at admin@csf.no </h5>");
	     l.setContentMode(Label.CONTENT_XHTML);	
	     if(helpLayout !=null)
	    	 root1.removeComponent(helpLayout);
	     if(helpNote != null)
	    	 helpLayout.removeComponent(helpNote);
	     helpNote = help.getHelpNote(l);	
	     helpLayout = new VerticalLayout();
	     helpLayout.setWidth("100%");
	     
	     
	     // Create the Form
	     newLoginForm = new Form();      
	     // Determines which properties are shown, and in which order:	        
	     usernameField = new TextField("Email:");
	     
	      signInButton = new Button("Sign In");
	     usernameField.setStyle(Reindeer.TEXTFIELD_SMALL);
	     usernameField.addListener(new TextChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void textChange(TextChangeEvent event) {
				// TODO Auto-generated method stub
				signInButton.setClickShortcut(KeyCode.ENTER);
				
			}
		});
	     passwordField = new PasswordField ("Password:");        
	     passwordField.setStyle(Reindeer.TEXTFIELD_SMALL);
	    
	    
	     signInButton.setStyle(Reindeer.BUTTON_SMALL);
	     newLoginForm.addField(Integer.valueOf(1), usernameField);
	     newLoginForm.addField(Integer.valueOf(2), passwordField);
	     newLoginForm.addField(Integer.valueOf(3), signInButton);
	     usernameField.setRequired(true);
	      passwordField.setRequired(true);
	     passwordField.setRequiredError("Please Enter a Valid Password! ");
	     usernameField.setRequiredError("Please Enter a Valid Username! ");
	     // Add form to layout
	     root1.addComponent(newLoginForm);	   
	     root1.addComponent(helpLayout);
	     helpLayout.addComponent(helpNote);
	     helpLayout.setComponentAlignment(helpNote, Alignment.MIDDLE_RIGHT);
		    
	     
	    
	     signInButton.addListener(this);
	     //signInButton.setVisible(true);
	     // Create the Upload component. 
	}
	@SuppressWarnings("deprecation")
	private void initRoot2()//Logged in user layout
	{
		if(signOutButton != null)
			this.removeComponent(signOutButton);
		if(root2 != null)
			root2.removeAllComponents();
		root2 = new Panel("WELCOME "+user.getUsername().toUpperCase());
		root2.setStyle(Reindeer.PANEL_LIGHT); 
		 // Tab 1 content
        l1 = new VerticalLayout();
        l1.setMargin(true);
        l1.setHeight("100%");
        this.initLayout1();
        l1.addComponent(layout1);
        
        // Tab 2 content
        l2 = new VerticalLayout();
        l2.setMargin(true);
        l2.setHeight("100%");
        
     // Tab 3 content
        l3 = new VerticalLayout();
        l3.setMargin(true);
        l3.setHeight("100%");
        
      
       // layout2.addComponent(t);
        t.setStyle(Reindeer.TABSHEET_MINIMAL);
        t.setHeight("100%");
        t.setWidth("90%");
        t1 = t.addTab(l1, "Experiment Handler", null);
        t2 = t.addTab(l2, "Change Password", null);
        if(user.isAdmin()) //add user form
		{
        	t3 = t.addTab(l3, "Admin", null);	
		}
        
        t.addListener(this);        
        root2.addComponent(t);
        
        t.setSelectedTab(t1);	
		
		
		signOutButton = new Button("Sign Out");
		signOutButton.setStyle(BaseTheme.BUTTON_LINK);
		signOutButton.setWidth("10%");
		this.addComponent(signOutButton);
		this.setComponentAlignment(signOutButton, Alignment.TOP_RIGHT);	
		signOutButton.addListener(new ClickListener(){
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				user = null;
				LoginView.super.removeComponent(root2);
				signOutButton.setVisible(false);
				LoginView.super.addComponent(root1);					
				
			}});
	
		
		
	}
	@SuppressWarnings("deprecation")
	private VerticalLayout initAdminLayout()//admin layout
	{
		adminLayout = new VerticalLayout();
		if(hsplit != null)
			adminLayout.removeComponent(hsplit);
    	hsplit = new HorizontalSplitPanel(); 
    	hsplit.setHeight("300px");
    	hsplit.setSplitPosition(50.0f);
    	hsplit.setStyle(Reindeer.SPLITPANEL_SMALL);
    	if(leftSide != null)
    		hsplit.removeComponent(leftSide);
    	if(rightSide != null)
    		hsplit.removeComponent(rightSide);
    	leftSide = new VerticalLayout();
    	this.updateRightSide();
    	hsplit.addComponent(leftSide);
    	hsplit.addComponent(rightSide);
    	
		 addUserLabel = new Label("<h4 style='color:blue'>Add New User</h4>");
		 addUserLabel.setContentMode(Label.CONTENT_XHTML);
		if(addUserLabel != null)
			leftSide.removeComponent(addUserLabel);
		leftSide.addComponent(addUserLabel);
		addUserLabel.setWidth("50%");
		leftSide.setComponentAlignment(addUserLabel,Alignment.MIDDLE_CENTER);
    	
		 Form newRegForm = new Form(); 
		 
		 newRegForm.setWidth("50%");
		 
		 final TextField emailField = new TextField("Email");
		 emailField.setStyle(Reindeer.TEXTFIELD_SMALL);
		 emailField.setRequired(true);
		final TextField usernameField = new TextField("Username");
		usernameField.setStyle(Reindeer.TEXTFIELD_SMALL);
		usernameField.setRequired(true);
	    final PasswordField  passwordField= new PasswordField("Password");
	    passwordField.setRequired(true);
	    passwordField.setStyle(Reindeer.TEXTFIELD_SMALL);
	    final PasswordField  rePasswordField= new PasswordField("Re-Password");
	    rePasswordField.setRequired(true);
	    rePasswordField.setStyle(Reindeer.TEXTFIELD_SMALL);
	    Button regButton = new Button("Regester");
	    regButton.setStyle(Reindeer.BUTTON_SMALL);
	    newRegForm.addField(Integer.valueOf(1), emailField);
	    newRegForm.addField(Integer.valueOf(2), usernameField);
	    newRegForm.addField(Integer.valueOf(3), passwordField);
	    newRegForm.addField(Integer.valueOf(4), rePasswordField);
	    newRegForm.addField(Integer.valueOf(5), regButton);
	    regButton.addListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				String username = (String)usernameField.getValue();
				String password = (String)passwordField.getValue();
				String rePassword = (String)rePasswordField.getValue();
				String email = (String)emailField.getValue();
				boolean valid = false;
				if(email == null || email.equals(""))
				{
					valid = false;
				}
				else if(! auth.validEmail(email))
				{
					valid =false;
				}
				else if(username == null || username.equals(""))
				{
					valid = false;
					
				}
				else if((password == null || password.equals("")))
				{
					valid = false;				
				}
				else if((rePassword == null || ! rePassword.equals(password)))
				{
					valid = false;
				}
				else
				{
						valid = auth.reg(username.toUpperCase(),password,false,email);								
				}
				if(valid == false){	
				
					if(error != null)
						adminLayout.removeComponent(error);
					 error = new Label("<h4 style='color:red'>Failed to Register The User Please Check The User Email, Username, Password,  Re-Password ! </h4>");
					 error.setContentMode(Label.CONTENT_XHTML);
					 adminLayout.addComponent(error);
				}else
				{
					emailField.setValue("");
					usernameField.setValue("");
					passwordField.setValue("");
					rePasswordField.setValue("");
					if(error != null)
						adminLayout.removeComponent(error);
					getWindow().showNotification("User Successfully Registered! ");
					hsplit.removeComponent(rightSide);
					updateRightSide();
					hsplit.addComponent(rightSide);	
				}
				
			}
		} );	    
	     
	    leftSide.addComponent(newRegForm);
	    leftSide.setComponentAlignment(newRegForm,Alignment.MIDDLE_CENTER);
	    adminLayout.addComponent(hsplit);
		return adminLayout;
	    
	    
		
	}
	@SuppressWarnings("deprecation")
	private void updateRightSide()//Handle remove user by admin
	{
		if(rightSide != null)
			rightSide.removeAllComponents();
		rightSide = new VerticalLayout();	
		
		
		removeUserLabel = new Label("<h4 style='color:blue'>Remove Existing User</h4>");
	    removeUserLabel.setContentMode(Label.CONTENT_XHTML);
	    removeUserLabel.setWidth("50%");
		
	    rightSide.addComponent(removeUserLabel);
		rightSide.setComponentAlignment(removeUserLabel, Alignment.MIDDLE_CENTER);//(removeUserLabel, ALIGNMENT_VERTICAL_CENTER, ALIGNMENT_TOP);
    	
		if(removeUserForm != null)
			rightSide.removeComponent(removeUserForm);
		removeUserForm = new Form(); 
		removeUserForm.setWidth("50%");
				usersList = auth.getUsersList();
	    select = new Select("Exist Users", usersList.values());		
	    removeUserForm.addField(Integer.valueOf(1),select);          	
	    Button removeButton = new Button("Remove");
	    removeButton.setStyle(Reindeer.BUTTON_SMALL);
	    removeUserForm.addField(Integer.valueOf(2), removeButton);
	    removeButton.addListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				String user = (String)select.getValue();
				if(user != null)
				{
					boolean test = auth.removeUser(user);
					if(test)
					{
						hsplit.removeComponent(rightSide);
						updateRightSide();
						hsplit.addComponent(rightSide);	
						getWindow().showNotification("User Successfully Removed! ");
			        	
					}
					else
					{

						getWindow().showNotification("Failed to Remove User! ");
					}
				}
				else{
		        	
				}
				
			}
		} );
	    rightSide.addComponent(removeUserForm);
	    rightSide.setComponentAlignment(removeUserForm, Alignment.MIDDLE_CENTER);//(removeUserLabel, ALIGNMENT_VERTICAL_CENTER, ALIGNMENT_TOP);
	
	    if(removeExperimentLayout != null)
	    	rightSide.removeComponent(removeExperimentLayout);
	    removeExperimentLayout = getRemoveForm("admin");
	    rightSide.addComponent(removeExperimentLayout);
	   
	
	
	}
	@SuppressWarnings("deprecation")
	private void initChangePasswordPanel()//Initialise change password panel
	{
		if(changePasswordPanel != null)
			changePasswordPanel.removeAllComponents();
		changePasswordPanel = new VerticalLayout();		
		changePasswordPanel.setWidth("30%");
		oldPasswordField = new PasswordField("Old Password");
		oldPasswordField.setRequired(true);
		oldPasswordField.setStyle(Reindeer.TEXTFIELD_SMALL);
		newPasswordField = new PasswordField("New Password");
		newPasswordField.setRequired(true);
		newPasswordField.setStyle(Reindeer.TEXTFIELD_SMALL);
		changePasswordButton = new Button("Change Password");
		changePasswordButton.setStyle(Reindeer.BUTTON_SMALL);
		Form changePassForm = new Form();		
		changePassForm.addField(Integer.valueOf(1),oldPasswordField); 
		changePassForm.addField(Integer.valueOf(2),newPasswordField);  
		changePassForm.addField(Integer.valueOf(3),changePasswordButton); 
		changePasswordPanel.addComponent(changePassForm);
		changePasswordPanel.setComponentAlignment(changePassForm, Alignment.BOTTOM_RIGHT);
		changePasswordButton.addListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				String name = user.getUsername();
				String oldPass = (String)oldPasswordField.getValue();
				String newPass = (String)newPasswordField.getValue();
				boolean valid = false;
				if(name == null || name.equals(""))
				{
					valid = false;
					
				}
				else if((oldPass == null || oldPass.equals("")))
				{
					valid = false;				
				}
				else if((newPass == null || newPass.equals("")))
				{
					valid = false;
				}
				else
				{
						valid = auth.changePassword(name.toUpperCase(),oldPass,newPass);								
				}
				if(valid == false){	
				
					if(error != null)
						layout2.removeComponent(error);
					 error = new Label("<h4 style='color:red'>Failed to Change Password Please Check Your Old Password or Try Again Later ! </h4>");
					 error.setContentMode(Label.CONTENT_XHTML);
					 layout2.addComponent(error);
				}else
				{
					oldPasswordField.setValue("");
					newPasswordField.setValue("");
					
					if(error != null)
						layout2.removeComponent(error);
					getWindow().showNotification("Password Successfully Changed! ");
					
				}
				
			}
		});
		
		
		
		
	}

	//select between user tabs 
	public void selectedTabChange(SelectedTabChangeEvent event) {
		String c = t.getTab(event.getTabSheet().getSelectedTab()).getCaption();
        if(c.equals("Experiment Handler"))
        {
        	if(layout1 != null)
        		layout1.removeAllComponents(); 
        	this.initLayout1();
        	 
              l1.addComponent(layout1);
        	
        }        
          else if(c.equals("Change Password"))
          {
        	  if(layout2 != null)
        		  layout2.removeAllComponents(); 
      		  this.initLayout2();  	 
              l2.addComponent(layout2);
          }
        else
        {
        	if(layout3 != null)
        		layout3.removeAllComponents();
        	 this.initLayout3();         	
        	l3.addComponent(layout3);
        }
		
	}

	//add admin layout to admin tab
	private void initLayout3() {
		if(adminLayout != null)
			root2.removeComponent(adminLayout);
		adminLayout = this.initAdminLayout();
    	layout3.addComponent(adminLayout);
		
	}

	//add change password panel to change password tab
	private void initLayout2() {
		this.initChangePasswordPanel();
		layout2.addComponent(changePasswordPanel);	
	}

	//add upload unit to experiment handling tab
	private void initLayout1() {
		if(layout1 != null)
			layout1.removeAllComponents();
    	UploadUnit uploader = new UploadUnit(url,dbName,driver,userName, password,user,mainTab,t);
		layout1.addComponent(uploader);
		
		
	}
	
	
	@SuppressWarnings("deprecation")
	private VerticalLayout getRemoveForm(final String admin)
	{
		VerticalLayout removeExp = new VerticalLayout();
		Label removeExperimentLabel = new Label("<h4 style='color:blue'>Remove Existing Experiment</h4>");
		removeExperimentLabel.setContentMode(Label.CONTENT_XHTML);
		removeExperimentLabel.setWidth("50%");
		removeExp.addComponent(removeExperimentLabel);
		removeExp.setComponentAlignment(removeExperimentLabel, Alignment.MIDDLE_CENTER);//(removeUserLabel, ALIGNMENT_VERTICAL_CENTER, ALIGNMENT_TOP);
	    	
			if(removeExperimentForm != null)
				removeExp.removeComponent(removeExperimentForm);
			removeExperimentForm = new Form(); 
			removeExperimentForm.setWidth("50%");
			expList = eh.getExperiments(null);
		    List<String>strExpList = new ArrayList<String>();
		    if(admin.equals("admin")){
			    for(ExperimentBean exp:expList.values())
		        {
		        	String str = exp.getExpId()+"	"+exp.getName()+"	( "+exp.getUploadedByName()+" )";
		        	strExpList.add(str);
		        }
			}
		    else{
		    	  for(ExperimentBean exp:expList.values())
			        {
		    		  if(admin.equals(exp.getEmail())){
			        	String str = exp.getExpId()+"	"+exp.getName()+"	( "+exp.getUploadedByName()+" )";
			        	strExpList.add(str);
		    		  }
			        }
		    	
		    }
		     selectExp = new Select("Experiment ID", strExpList);	    
		    removeExperimentForm.addField(Integer.valueOf(1),selectExp);             	
		    Button removeExpButton = new Button("Remove Experiment");
		    removeExpButton.setStyle(Reindeer.BUTTON_SMALL);
		    removeExperimentForm.addField(Integer.valueOf(2), removeExpButton);
		    removeExpButton.addListener(new ClickListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					String str = selectExp.getValue().toString();
	        		String[] strArr =str.split("\t");
	        		Integer expId = (Integer.valueOf(strArr[0]));
					if(expId != null)
					{
						boolean test = auth.removeExp(expId);
						if(test && admin.equals("admin") )
						{
							hsplit.removeComponent(rightSide);
							updateRightSide();
							hsplit.addComponent(rightSide);	
							getWindow().showNotification("Experiment Successfully Removed! ");
				        	
						}
						else if(test)
						{
							initLayout1();
						}
						else
						{

							getWindow().showNotification("Failed to Remove The Experiment! ");
						}
					}
					else{
			        	
					}
					
				}
			} );
		    removeExp.addComponent(removeExperimentForm);
		    removeExp.setComponentAlignment(removeExperimentForm, Alignment.MIDDLE_CENTER);	
		
		    return removeExp;
	}

}
