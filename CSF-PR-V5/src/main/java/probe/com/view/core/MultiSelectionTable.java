/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.RowReference;
import com.vaadin.ui.Grid.RowStyleGenerator;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.SSLEngineResult.Status;

/**
 *
 * @author y-mok_000
 */
 @StyleSheet("itemhighlight.css")
public class MultiSelectionTable  extends VerticalLayout implements SelectionListener{

     
 
//        private Set<BeanItem<BugEntry>> editedItems = new HashSet<BeanItem<BugEntry>>();
//        private Set<BeanItem<BugEntry>> oldEditedItems = new HashSet<BeanItem<BugEntry>>();
 
        public MultiSelectionTable() {
            setSpacing(true);
            // Set up combo boxes with possible values         
            setWidth(100, Unit.PERCENTAGE); 
            // Editor is not enabled when
            setEnabled(false);
            
            Indexed datares=new BeanContainer(null);

 
        // Create new Grid with BeanItemContainer filled with BugEntries
        Grid grid = new Grid(datares);
        grid.setSizeFull();
 
        // Hide the "changed" property
        grid.removeColumn("changed");
 
        // Activate multi selection mode
        grid.setSelectionMode(SelectionMode.MULTI);
 
        // Add a rowstyle generator to display recently changed items.
        // Also uses another style for closed entries
        grid.setRowStyleGenerator(new RowStyleGenerator() {
 
            @Override
            public String getStyle(RowReference rowReference) {
                Item item = rowReference.getItem();
                boolean closed = item.getItemProperty("status").getValue() == Status.CLOSED;
                boolean changed = (Boolean) item.getItemProperty("changed")
                        .getValue();
                if (closed) {
                    if (changed) {
                        // Combined style
                        return "changed closed";
                    }
                    // Closed style
                    return "closed";
                } else if (changed) {
                    // Modified style
                    return "changed";
                }
                // No style
                return null;
            }
        });
 
        // Make Editor listen to changes in the selection of this Grid
        grid.addSelectionListener(this);
 
        }
 
        @Override
        public void select(SelectionEvent event) {
            // Logic for handling selection changes from Grid
            boolean empty = event.getSelected().isEmpty();
            if (!empty) {
                // Some items are selected. Make sure Editor is enabled
                setEnabled(true);
 
                // Keep track of items currently being edited.
            } else {
                // No items are selected. Disable editor.
                setEnabled(false);
            }
        }
 
        
    }
 
 