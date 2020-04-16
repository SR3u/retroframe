package org.sr3u.photoframe.client.ui.settings.filters;

import org.sr3u.photoframe.client.filters.FilterDescriptor;
import org.sr3u.photoframe.client.filters.ImageFilter;
import org.sr3u.photoframe.client.filters.ImageFilters;
import org.sr3u.photoframe.client.ui.main.ImageWindow;
import org.sr3u.photoframe.client.ui.settings.ScrollableWindow;
import org.sr3u.photoframe.server.Main;
import sr3u.streamz.streams.Streamex;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class FiltersWindow extends ScrollableWindow {

    private final List<FilterPanel> filterPanels;
    private final TechnicalFilterPanel technicalFilterPanel;
    private final ImageWindow imageWindow;

    private PanelDelegate panelDelegate = new PanelDelegate() {
        @Override
        public void add(TechnicalFilterPanel panel) {
            scrollPaneContent.remove(technicalFilterPanel);
            FilterPanel identity = new FilterPanel("identity", "");
            scrollPaneContent.add(identity);
            filterPanels.add(identity);
            identity.setDelegate(this);
            scrollPaneContent.add(technicalFilterPanel);
            forceRepaint();
        }

        @Override
        public void delete(FilterPanel panel) {
            scrollPaneContent.remove(panel);
            filterPanels.remove(panel);
            panel.setDelegate(null);
            forceRepaint();
        }

        @Override
        public void down(UpDownButtonsPanel panel) {
            //TODO
            forceRepaint();
        }

        @Override
        public void up(UpDownButtonsPanel panel) {
            //TODO
            forceRepaint();
        }

        @Override
        public void apply() {
            String newFilterChain = Streamex.ofStream(filterPanels.stream())
                    .map(FilterPanel::toFilterDescriptor)
                    .mapToString(FilterDescriptor::toString)
                    .joined(" | ");
            if (newFilterChain == null || newFilterChain.trim().isEmpty()) {
                newFilterChain = "identity";
            }
            try {
                ImageFilter parsed = ImageFilters.parse(newFilterChain);
                imageWindow.setImageFilter(parsed);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Failed to set filters:\n" + newFilterChain, "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Main.settings.getClient().setImageFitlerChain(newFilterChain);
        }

        @Override
        public void applyAndSave() {
            apply();
            Main.settings.save(Main.SETTINGS_PROPERTIES);
        }
    };

    private void forceRepaint() {
        frame.invalidate();
        frame.revalidate();
        frame.repaint();
    }

    public FiltersWindow(ImageWindow imageWindow) {
        super("Filters");
        this.imageWindow = imageWindow;
        String imageFitlerChain = Main.settings.getClient().getImageFitlerChain();
        List<FilterDescriptor> filterDescriptors = ImageFilters.parseDescriptors(imageFitlerChain);
        filterPanels = filterDescriptors.stream()
                .map(FilterPanel::new)
                .peek(fp -> fp.setMinimumSize(new Dimension(32, 32)))
                .peek(scrollPaneContent::add)
                .peek(fp -> fp.setDelegate(panelDelegate))
                .collect(Collectors.toList());
        technicalFilterPanel = new TechnicalFilterPanel();
        technicalFilterPanel.setDelegate(panelDelegate);
        scrollPaneContent.add(technicalFilterPanel);
        frame.pack();
        frame.setVisible(true);
    }
}