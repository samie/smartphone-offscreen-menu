package org.vaadin.jouni.samples.mobile;

import org.vaadin.jouni.samples.mobile.MobileNavigationSampleUI.AppView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class MobileView implements View {

    private static final long serialVersionUID = 4699956880116389256L;

    private String name;

    public MobileView(String name) {
        this.name = name;
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }

    public Component getContent() {
        return null;
    }

    public Resource getIcon() {
        return null;
    }

    /**
     * Return the titlebar for this view. Can be any Vaadin component, but
     * usually either a Label or a HorizontalLayout. Visually fixed at the top
     * of the screen.
     */
    public Component getTitleBar() {
        Label title = new Label(getName());
        title.addStyleName(ValoTheme.LABEL_LARGE);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        return title;
    }

    /**
     * Return the toolbar for this view. Usually either a set of Buttons in a
     * horizontal layout. Can be used to create a tab bar as well. Visually
     * fixed at the bottom of the screen.
     * 
     * @return
     */
    public Component getToolBar() {
        return null;
    }

    /**
     * Hide the global navigation menu toggle. Useful if you need to place some
     * other component in the titlebar in the same place (like a navigate-back
     * button).
     */
    public void hideMenuToggle() {
        if (getContent() != null && getContent().isAttached()) {
            ((AppView) getContent().getParent().getParent()).hideMenuToggle();
        }
    }

    public void showMenuToggle() {
        if (getContent() != null && getContent().isAttached()) {
            ((AppView) getContent().getParent().getParent()).showMenuToggle();
        }
    }

    public String getName() {
        return name;
    }

}
