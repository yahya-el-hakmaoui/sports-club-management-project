package com.clubsportif.ui.admin;

import com.clubsportif.model.User;
import com.clubsportif.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminPanel extends JPanel {
    private User user;

    public AdminPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // En-tête stylé avec logo admin et nom complet
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))
        ));
        headerPanel.setPreferredSize(new Dimension(100, 70));

        // Partie gauche : logo + nom
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 18));
        leftPanel.setOpaque(false);

        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(48, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);

        Icon adminIcon = new Icon() {
            public int getIconWidth() { return 48; }
            public int getIconHeight() { return 48; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(100, 149, 237));
                g2.fillOval(x, y, 48, 48);
                g2.setColor(Color.WHITE);
                int[] xs = {x+24, x+29, x+44, x+32, x+36, x+24, x+12, x+16, x+4, x+19};
                int[] ys = {y+8, y+20, y+20, y+28, y+44, y+34, y+44, y+28, y+20, y+20};
                g2.fillPolygon(xs, ys, xs.length);
                g2.dispose();
            }
        };
        iconLabel.setIcon(adminIcon);

        String nomComplet = (user != null ? user.getLastname() + " " + user.getName() : "");
        String nomMaj = nomComplet.toUpperCase();
        JLabel nameLabel = new JLabel("Bonjour, " + nomMaj);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nameLabel.setForeground(new Color(60, 63, 65));

        leftPanel.add(iconLabel);
        leftPanel.add(nameLabel);

        // Partie droite : rôle
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 18));
        rightPanel.setOpaque(false);

        String roleText = "";
        Color roleColor = Color.RED;
        if (user != null) {
            String role = user.getRole().toString().toUpperCase();
            roleText = role;
            if ("ADMIN".equals(role)) {
                roleColor = new Color(255, 0, 0); // Rouge foncé
            } else {
                roleColor = new Color(255, 99, 71); // Rouge clair (tomato)
            }
        }
        JLabel roleLabel = new JLabel(roleText);
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        roleLabel.setForeground(roleColor);

        rightPanel.add(roleLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setBackground(new Color(245, 245, 250));
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabbedPane.setForeground(new Color(60, 63, 65));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        UIManager.put("TabbedPane.selected", new Color(100, 149, 237));
        UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);

        tabbedPane.addTab("Accueil", UIManager.getIcon("FileView.homeFolderIcon"), new WelcomeAdminPanel(), "Accueil");
        tabbedPane.addTab("Activités", UIManager.getIcon("FileView.directoryIcon"), new ActivityAdminPanel(), "Gestion des activités");
        tabbedPane.addTab("Adhérents", UIManager.getIcon("FileView.fileIcon"), new AdherentsAdminPanel(), "Gestion des adhérents");
        tabbedPane.addTab("Paiements", UIManager.getIcon("FileView.hardDriveIcon"), new ParametresPaiementsAdminPanel(), "Gestion des paiements");

        // Ajout de l'onglet Assistant uniquement pour les admins
        if (user != null && user.getRole() == com.clubsportif.model.User.Role.admin) {
            tabbedPane.addTab("Assistant", UIManager.getIcon("FileView.computerIcon"), new AssistantAdminPanel(), "Gestion des assistants");
        }

        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                return 160;
            }
            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return 48;
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        // --- Bouton Déconnexion en bas à gauche, non collé au coin ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 250));
        JPanel logoutWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoutWrapper.setBackground(new Color(245, 245, 250));
        logoutWrapper.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 0));

        JButton logoutButton = new JButton("Se déconnecter");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener((ActionEvent e) -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof MainFrame) {
                ((MainFrame) window).showPanel(MainFrame.LOGIN_PANEL, new com.clubsportif.ui.LoginPanel((MainFrame) window));
            }
        });

        logoutWrapper.add(logoutButton);
        bottomPanel.add(logoutWrapper, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
