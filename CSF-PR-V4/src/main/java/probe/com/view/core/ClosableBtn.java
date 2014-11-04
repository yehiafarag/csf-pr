/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author y-mok_000
 */
public  class ClosableBtn extends Button{
         private final String label;
         private final int filterId;
         @SuppressWarnings("LeakingThisInConstructor")
         public ClosableBtn(String label,int filterId,boolean closable)
         {
             super(label);
             this.filterId = filterId;
             this.label = label;
            
              this.setStyleName(Reindeer.BUTTON_SMALL);
              this.setHeight("20px"); 
              if(closable){
              this.setIcon(new ThemeResource("img/Close.png"));
             }
         }
     @Override
     public String toString(){
         return label;
     
     }

    public int getFilterId() {
        return filterId;
    }

}
     
     
     
