package org.vaadin.jouni.samples.mobile;

import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.vaadin.jouni.samples.mobile.view.FeedView;
import org.vaadin.jouni.samples.mobile.view.HomeView;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("mobile-navigation-sample")
@Title("Mobile Navigation Sample")
@JavaScript({ "fastclick.js" })
public class MobileNavigationSampleUI extends UI {

    /**
     * A custom servlet with a custom SessionInitListener to allow us to modify
     * the bootstrap HTML and include the necessary viewport meta tags for
     * smartphone support.
     */
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MobileNavigationSampleUI.class)
    public static class Servlet extends VaadinServlet {
        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
            getService()
                    .addSessionInitListener(new MobileSessionInitListener());
        }
    }

    /**
     * List of views in the application, mapped to their name in the menu
     */
    private LinkedList<MobileView> views = new LinkedList<MobileView>() {
        {
            add(new HomeView());
            add(new FeedView());
        }
    };

    /**
     * The Navigator instance to control the view state
     */
    Navigator navigator;

    /**
     * A simple ViewDisplay for switching the content of the UI, while the main
     * menu stays in place.
     * 
     * @author jouni
     * 
     */
    class AppView extends CssLayout implements ViewDisplay {

        // We need extra containers for each part of the view because of CSS
        // transitions
        CssLayout titlebarContainer = new CssLayout();
        CssLayout contentContainer = new CssLayout();
        CssLayout toolbarContainer = new CssLayout();

        public AppView(Layout menu) {
            setPrimaryStyleName("app-view");
            setWidth("100%");

            menu.setWidth("100%");
            menu.addStyleName("app-menu");
            addComponent(menu);

            contentContainer.setPrimaryStyleName("app-content");
            contentContainer.setWidth("100%");
            contentContainer.setId("app-content");
            addComponent(contentContainer);

            titlebarContainer.setPrimaryStyleName("app-titlebar");
            titlebarContainer.setWidth("100%");
            addComponent(titlebarContainer);

            toolbarContainer.setPrimaryStyleName("app-toolbar");
            toolbarContainer.setWidth("100%");
            addComponent(toolbarContainer);

            /**
             * The menu icon button which toggles the visibility of the menu.
             */
            Button toggleMenu = new Button();
            toggleMenu.setPrimaryStyleName("app-menu-toggle");
            toggleMenu.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    toggleMenu();
                }
            });
            addComponent(toggleMenu);
        }

        public void showMenu() {
            addStyleName("app-menu-visible");
        }

        public void hideMenu() {
            removeStyleName("app-menu-visible");
        }

        public void toggleMenu() {
            if (getStyleName().contains("app-menu-visible")) {
                hideMenu();
            } else {
                showMenu();
            }
        }

        public void hideMenuToggle() {
            addStyleName("app-menu-toggle-hidden");
        }

        public void showMenuToggle() {
            removeStyleName("app-menu-toggle-hidden");
        }

        @Override
        public void showView(View newView) {
            if (newView instanceof MobileView) {

                // Remove old view parts
                titlebarContainer.removeAllComponents();
                contentContainer.removeAllComponents();
                toolbarContainer.removeAllComponents();

                // Add the new view parts
                MobileView view = (MobileView) newView;
                contentContainer.addComponent(view.getContent());

                if (view.getTitleBar() != null) {
                    titlebarContainer.addComponent(view.getTitleBar());
                }

                if (view.getToolBar() != null) {
                    addComponent(view.getToolBar());
                }

                // Scroll the view to top
                Page.getCurrent()
                        .getJavaScript()
                        .execute(
                                "document.body.scrollTop = document.getElementById('app-content').scrollTop = 0;");

                // Restore menu toggle visibility
                showMenuToggle();
            } else {
                throw new IllegalArgumentException("View is not a MobileView: "
                        + newView);
            }
        }
    }

    AppView appView = null;

    @Override
    protected void init(VaadinRequest request) {
        // Remove 300ms tap/click delay from mobile browsers
        Page.getCurrent().getJavaScript()
                .execute("FastClick.attach(document.body);");

        final CssLayout menu = new CssLayout() {
            {
                addStyleName(ValoTheme.MENU_ROOT);
                /**
                 * Build the list of buttons for the menu, which trigger the
                 * view changes.
                 */
                for (final MobileView view : views) {
                    Button menuItem = new Button(view.getName());
                    menuItem.setIcon(view.getIcon());
                    menuItem.setPrimaryStyleName(ValoTheme.MENU_ITEM);
                    menuItem.addClickListener(new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            // Navigate to the selected view
                            navigator.navigateTo(getUri(view.getName()));
                        }
                    });
                    addComponent(menuItem);
                }
            }
        };
        appView = new AppView(menu);

        // Set the UI content
        setContent(appView);

        // Set up the navigator, bind it to this UI and our AppView
        navigator = new Navigator(this, (ViewDisplay) appView);
        navigator.setErrorView(new MobileView("Not Found") {
            @Override
            public void enter(ViewChangeEvent event) {
                Notification.show("Sorry, no view found for "
                        + event.getViewName());
            }
        });

        // Add all views to the Navigator
        for (MobileView view : views) {
            navigator.addView(getUri(view.getName()), view);
        }

        // We need a view change listener to update the selected state of the
        // menu items
        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                // Allow the view change
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                // Update the selection state of the menu items
                for (Iterator<Component> it = menu.iterator(); it.hasNext();) {
                    Component item = it.next();
                    if (getUri(item.getCaption()).equals(event.getViewName())) {
                        item.addStyleName("selected");
                    } else {
                        item.removeStyleName("selected");
                    }
                }

                // Close the menu
                appView.hideMenu();
            }
        });

        // Open the default view
        if (navigator.getState() == null || navigator.getState().equals("")) {
            navigator.navigateTo(getUri(HomeView.NAME));
        }

    }

    String getUri(String name) {
        // TODO replace all special characters that are not allowed in a URI
        return name.toLowerCase().replace(" ", "-");
    }
}