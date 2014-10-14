package org.vaadin.jouni.samples.mobile.view;

import org.vaadin.jouni.samples.mobile.MobileView;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class FeedView extends MobileView {

    private static final long serialVersionUID = -5520896253186777767L;

    public static final String NAME = "Feed";

    VerticalLayout content = new VerticalLayout() {

        {
            setMargin(true);
            setSpacing(true);

            Label header = new Label("The " + getName() + " view");
            header.addStyleName(ValoTheme.LABEL_H2);
            header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
            addComponent(header);

            addComponent(new Label("Tap the menu icon to switch views."));
            addComponent(new Button("Move to sub-view", new ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    contentWrapper.removeAllComponents();
                    contentWrapper.addComponent(anotherContent);
                    back.setVisible(true);
                    title.setValue("Sub-feed");
                    hideMenuToggle();
                }
            }));
            addComponent(new Label(
                    "<div><h3>More content to demo scrolling.</h3><p><span>Curabitur blandit tempus ardua ridiculus sed magna.</span> <span>Phasellus laoreet lorem vel dolor tempus vehicula.</span> <span>Plura mihi bona sunt, inclinet, amari petere vellent.</span> <span>Quis aute iure reprehenderit in voluptate velit esse.</span> <span>At nos hinc posthac, sitientis piros Afros.</span> <span>Unam incolunt Belgae, aliam Aquitani, tertiam.</span></p><p><span>At nos hinc posthac, sitientis piros Afros.</span> <span>Curabitur blandit tempus ardua ridiculus sed magna.</span> <span>Tu quoque, Brute, fili mi, nihil timor populi, nihil!</span> <span>Magna pars studiorum, prodita quaerimus.</span></p><p><span>Etiam habebis sem dicantur magna mollis euismod.</span> <span>Sed haec quis possit intrepidus aestimare tellus.</span> <span>Qui ipsorum lingua Celtae, nostra Galli appellantur.</span> <span>Quisque ut dolor gravida, placerat libero vel, euismod.</span> <span>Morbi fringilla convallis sapien, id pulvinar odio volutpat.</span></p></div>",
                    ContentMode.HTML));
        }
    };

    VerticalLayout anotherContent = new VerticalLayout() {
        {
            setMargin(true);
            setSpacing(true);

            addComponent(new Label(
                    "A sub-view for the Feed view. Click the back arrow or the button below to go back"));
            addComponent(new Button("Go Back", new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    goBack();
                }
            }));
        }
    };

    Button back = new Button(null, FontAwesome.CHEVRON_LEFT);
    Label title = new Label();

    CssLayout contentWrapper = new CssLayout();

    public FeedView() {
        super(NAME);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        contentWrapper.removeAllComponents();
        contentWrapper.addComponent(content);
    }

    @Override
    public Component getContent() {
        return contentWrapper;
    }

    @Override
    public Resource getIcon() {
        return FontAwesome.LIST_ALT;
    }

    void goBack() {
        contentWrapper.removeAllComponents();
        contentWrapper.addComponent(content);
        back.setVisible(false);
        title.setValue(getName());
        showMenuToggle();
    }

    @Override
    public Component getTitleBar() {
        return new CssLayout() {
            {
                setWidth("100%");

                addComponent(back);
                back.setPrimaryStyleName("app-titlebar-button");
                back.addStyleName("left");
                back.setVisible(false);
                back.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        goBack();
                    }
                });

                title.setValue(getName());
                title.setSizeUndefined();
                title.addStyleName(ValoTheme.LABEL_LARGE);
                title.addStyleName(ValoTheme.LABEL_LIGHT);
                addComponent(title);
            }
        };
    }
}
