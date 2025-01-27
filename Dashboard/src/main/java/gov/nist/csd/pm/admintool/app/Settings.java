package gov.nist.csd.pm.admintool.app;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import gov.nist.csd.pm.admintool.actions.Action;
import gov.nist.csd.pm.admintool.services.RestService;


@Tag("settings")
public class Settings extends VerticalLayout {
    private HorizontalLayout layout;

    public Settings() {
        layout = new HorizontalLayout();
        layout.setFlexGrow(1.0);

        add(new H2("Settings:"));
        add(new Paragraph("\n"));

        add(layout);
        setUpLayout();
    }

    private void setUpLayout() {
        setSizeFull();
        setPadding(true);

        // Creating URL Text Field
        TextField URLInput = new TextField();

        // Creating Each Icon
        Icon loadingIcon = new Icon(VaadinIcon.COG);
        Icon checkIcon = new Icon(VaadinIcon.CHECK);
        checkIcon.setColor("green");
        Icon closeIcon = new Icon(VaadinIcon.CLOSE);
        closeIcon.setColor("red");

        // Creating and Configuring Text and Icon Horizontal Layout
        HorizontalLayout textAndIcon = new HorizontalLayout(URLInput, loadingIcon);
        textAndIcon.setAlignItems(Alignment.CENTER);

        // Configuring URL Text Field
        URLInput.addValueChangeListener((event) -> {
            if (event.getValue() == null || event.getValue().equals(""))
                Action.coordinatorURL = null;
            else {
                Action.coordinatorURL = event.getValue();
                if (RestService.checkCoordinatorStatus(Action.coordinatorURL + "/status")) {
                    textAndIcon.removeAll();
                    textAndIcon.add(URLInput, checkIcon);
                }
                else {
                    textAndIcon.removeAll();
                    textAndIcon.add(URLInput, closeIcon);
                }
            }
        });
        URLInput.setWidth("100%");

        // Creating and Configuring Details Dropdown
        Details coordinatorURL = new Details("Coordinator URL", null);
        coordinatorURL.setContent(textAndIcon);
        coordinatorURL.getElement().getStyle()
                .set("background", "#DADADA"); //#A0FFA0
        coordinatorURL.addThemeVariants(DetailsVariant.FILLED);
        coordinatorURL.addOpenedChangeListener(e -> {
            if (e.isOpened()) {
                if (Action.coordinatorURL == null)
                    URLInput.setValue("");
                else {
                    URLInput.setValue(Action.coordinatorURL);
                    if (RestService.checkCoordinatorStatus(Action.coordinatorURL + "/status")) {
                        textAndIcon.removeAll();
                        textAndIcon.add(URLInput, checkIcon);
                    }
                    else {
                        textAndIcon.removeAll();
                        textAndIcon.add(URLInput, closeIcon);
                    }
                }
            }
        });
        coordinatorURL.setOpened(true);

        add(coordinatorURL);
    }
}
