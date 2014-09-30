package org.vaadin.jouni.samples.mobile.view;

import org.vaadin.jouni.samples.mobile.MobileView;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class HomeView extends MobileView {

    private static final long serialVersionUID = -2762299957808959424L;

    public static final String NAME = "Home";

    VerticalLayout content = new VerticalLayout() {
        {
            setMargin(true);
            setSpacing(true);

            Label header = new Label("The " + getName() + " view");
            header.addStyleName(ValoTheme.LABEL_H2);
            header.addStyleName(ValoTheme.LABEL_NO_MARGIN);
            addComponent(header);

            addComponent(new Label(
                    "A simple tech demo for a mobile navigation pattern. Tap the menu icon to switch views."));
            Label info = new Label(
                    "Built with <a href=\"https://vaadin.com\">Vaadin</a>. Available on <a href=\"https://www.github.com/jounik/mobile-navigation-sample\">GitHub</a>.",
                    ContentMode.HTML);
            addComponent(info);
            addComponent(new Label(
                    "<div><h3>More content to demonstrate scrolling</h3><p><span>Curabitur blandit tempus ardua ridiculus sed magna.</span> <span>Phasellus laoreet lorem vel dolor tempus vehicula.</span> <span>Plura mihi bona sunt, inclinet, amari petere vellent.</span> <span>Quis aute iure reprehenderit in voluptate velit esse.</span> <span>At nos hinc posthac, sitientis piros Afros.</span> <span>Unam incolunt Belgae, aliam Aquitani, tertiam.</span></p><p><span>At nos hinc posthac, sitientis piros Afros.</span> <span>Curabitur blandit tempus ardua ridiculus sed magna.</span> <span>Tu quoque, Brute, fili mi, nihil timor populi, nihil!</span> <span>Magna pars studiorum, prodita quaerimus.</span></p><p><span>Etiam habebis sem dicantur magna mollis euismod.</span> <span>Sed haec quis possit intrepidus aestimare tellus.</span> <span>Qui ipsorum lingua Celtae, nostra Galli appellantur.</span> <span>Quisque ut dolor gravida, placerat libero vel, euismod.</span> <span>Morbi fringilla convallis sapien, id pulvinar odio volutpat.</span></p></div>",
                    ContentMode.HTML));
        }
    };

    public HomeView() {
        super(NAME);
    }

    @Override
    public Component getContent() {
        return content;
    }

    @Override
    public Resource getIcon() {
        return FontAwesome.HOME;
    }
}
