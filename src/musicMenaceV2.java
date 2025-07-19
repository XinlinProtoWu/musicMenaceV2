
import com.sun.javafx.application.PlatformImpl;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.RowFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;





/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author 吴欣霖
 */

/**
 * Color pallette
 * Black: [23, 23, 23]
 * Brown: [89,61,62]
 * Red: [193,49,42]
 * Gold: [208, 158, 57]
 * White:[229,220,225]
 */
public class musicMenaceV2 extends javax.swing.JFrame {

    /**
     * Creates new form musicMenaceV2
     */
    //Declare global variables
    private String currentUser;
    private javax.swing.Timer timer;
    private boolean isPlaying=false;
    private Media sound;
    private String playingMusic;
    private MediaPlayer mediaPlayer;
    private ArrayList<Music> musicList = new ArrayList<Music>();
    //Body
    public musicMenaceV2() {
        initComponents();
        //Initialize timer for progress bar
        initTimer();
        //Initialize playlist
        File userFolder = new File("data/"+currentUser+"/music");
        importFolder(userFolder);
        //Set styling of table
        songDisplay.setBackground(new Color(23, 23, 23));
        ((DefaultTableCellRenderer)songDisplay.getDefaultRenderer(Object.class)).setBackground(new Color(23, 23, 23));
        songDisplay.setGridColor(new Color(23, 23, 23));
        songDisplay.setForeground(new Color(208,158,57));
        songDisplay.setFont(new java.awt.Font("Arial Unicode MS", 0, 24));
        songDisplay.getTableHeader().setFont(new java.awt.Font("Arial Unicode MS", 0, 24));
        ((DefaultTableCellRenderer)songDisplay.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        songDisplay.getTableHeader().setForeground(new Color(208,158,57));
        songDisplay.getTableHeader().setFont(new java.awt.Font("Arial Unicode MS", 0, 24));
        songDisplay.getTableHeader().setOpaque(false);
        songDisplay.getTableHeader().setBackground(new Color(23, 23, 23));
        jScrollPane1.setBackground(new Color(23, 23, 23));
        jScrollPane1.getViewport().setBackground(new Color(23, 23, 23));
        TableColumnModel colMdl = songDisplay.getColumnModel();
        colMdl.getColumn(0).setPreferredWidth(500);
        colMdl.getColumn(1).setPreferredWidth(10);
        colMdl.getColumn(2).setPreferredWidth(10);
        colMdl.setColumnSelectionAllowed(false);
        
        //Set background image
        setPanelBackgroundImage(jPanel1, "/imgs/back.PNG");
        
        //Set icon of rewind button
        ImageIcon originalIconBack = new ImageIcon(getClass().getResource("/imgs/backBtn.png"));
        Image originalImageBack = originalIconBack.getImage();

        // Set the desired width and height for the scaled image
        int scaledWidth = 50;
        int scaledHeight = 50;

        // Create a scaled instance of the image
        Image scaledImageBack = originalImageBack.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        // Create a new ImageIcon with the scaled image
        ImageIcon scaledIconBack = new ImageIcon(scaledImageBack);

        // Set the scaled icon to the button
        rewindBtn.setIcon(scaledIconBack);
        rewindBtn.setText("");  // Set the text to an empty string
        rewindBtn.setContentAreaFilled(false);  // Disable content area

        // If you also want to remove the border
        rewindBtn.setBorderPainted(false);
        
        //FastForward
        ImageIcon originalIconForward = new ImageIcon(getClass().getResource("/imgs/fastForward.png"));
        Image originalImageForward = originalIconForward.getImage();

        // Create a scaled instance of the image
        Image scaledImageForward = originalImageForward.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        // Create a new ImageIcon with the scaled image
        ImageIcon scaledIconForward = new ImageIcon(scaledImageForward);

        // Set the scaled icon to the button
        fastForwardBtn.setIcon(scaledIconForward);
        fastForwardBtn.setText("");  // Set the text to an empty string
        fastForwardBtn.setContentAreaFilled(false);  // Disable content area

        // If you also want to remove the border
        fastForwardBtn.setBorderPainted(false);
        
        //playPause
        ImageIcon originalIconpp = new ImageIcon(getClass().getResource("/imgs/playPause.png"));
        Image originalImagepp = originalIconpp.getImage();

        // Create a scaled instance of the image
        Image scaledImagepp = originalImagepp.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        // Create a new ImageIcon with the scaled image
        ImageIcon scaledIconpp = new ImageIcon(scaledImagepp);

        // Set the scaled icon to the button
        playPauseBtn.setIcon(scaledIconpp);
        playPauseBtn.setText("");  // Set the text to an empty string
        playPauseBtn.setContentAreaFilled(false);  // Disable content area

        // If you also want to remove the border
        playPauseBtn.setBorderPainted(false);
    }
    //Used for setting panel background
    private void setPanelBackgroundImage(javax.swing.JPanel panel, String imagePath) {
        // Load the image
        ImageIcon imageIcon = loadImageIcon(imagePath);

        // Create a JLabel with the image
        JLabel imageLabel = new JLabel(imageIcon);

        // Set layout to null to allow absolute positioning
        panel.setLayout(null);

        // Set the bounds of the imageLabel to cover the whole panel
        imageLabel.setBounds(0, 0, panel.getWidth(), panel.getHeight());

        // Add the imageLabel to the panel
        panel.add(imageLabel);
    }
    //Used for loading image
    private ImageIcon loadImageIcon(String imagePath) {
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            return new ImageIcon(imageUrl);
        } else {
            return null;
        }
    }
    //Setting the table model for the playlist and disabling user edits
    private static DefaultTableModel model = new DefaultTableModel(
        new Object[][]{},
        new String[]{"Title", "Artist", "Duration"}
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        currentUserDisp1 = new javax.swing.JLabel();
        currentUserDisp2 = new javax.swing.JLabel();
        fileBtn = new javax.swing.JButton();
        folderBtn = new javax.swing.JButton();
        sortBox = new javax.swing.JComboBox<>();
        currentUserDisp3 = new javax.swing.JLabel();
        searchBox = new javax.swing.JTextField();
        currentUserDisp4 = new javax.swing.JLabel();
        convertBtn = new javax.swing.JButton();
        convertBox = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        rewindBtn = new javax.swing.JButton();
        fastForwardBtn = new javax.swing.JButton();
        playPauseBtn = new javax.swing.JButton();
        maxTimeDisp = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        currentUserDisp = new javax.swing.JLabel();
        delBtn = new javax.swing.JButton();
        logOutBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        songDisplay = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        sortBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(23, 23, 23));

        currentUserDisp1.setFont(new java.awt.Font("Rockwell", 0, 24)); // NOI18N
        currentUserDisp1.setForeground(new java.awt.Color(204, 175, 131));

        currentUserDisp2.setBackground(new java.awt.Color(240, 206, 154));
        currentUserDisp2.setFont(new java.awt.Font("Rockwell", 0, 36)); // NOI18N
        currentUserDisp2.setForeground(new java.awt.Color(208, 158, 57));
        currentUserDisp2.setText("Music Menace V2");

        fileBtn.setBackground(new java.awt.Color(23, 23, 23));
        fileBtn.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        fileBtn.setForeground(new java.awt.Color(208, 158, 57));
        fileBtn.setText("Import File");
        fileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileBtnActionPerformed(evt);
            }
        });

        folderBtn.setBackground(new java.awt.Color(23, 23, 23));
        folderBtn.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        folderBtn.setForeground(new java.awt.Color(208, 158, 57));
        folderBtn.setText("Import Folder");
        folderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                folderBtnActionPerformed(evt);
            }
        });

        sortBox.setBackground(new java.awt.Color(23, 23, 23));
        sortBox.setFont(new java.awt.Font("Rockwell", 0, 18)); // NOI18N
        sortBox.setForeground(new java.awt.Color(208, 158, 57));
        sortBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Title", "Artist", "Duration" }));

        currentUserDisp3.setFont(new java.awt.Font("Rockwell", 0, 24)); // NOI18N
        currentUserDisp3.setForeground(new java.awt.Color(208, 158, 57));
        currentUserDisp3.setText("Sort By:");

        searchBox.setBackground(new java.awt.Color(23, 23, 23));
        searchBox.setFont(new java.awt.Font("Arial Unicode MS", 0, 14));
        searchBox.setForeground(new java.awt.Color(208, 158, 57));
        searchBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchBoxKeyTyped(evt);
            }
        });

        currentUserDisp4.setBackground(new java.awt.Color(240, 218, 168));
        currentUserDisp4.setFont(new java.awt.Font("Rockwell", 0, 24)); // NOI18N
        currentUserDisp4.setForeground(new java.awt.Color(208, 158, 57));
        currentUserDisp4.setText("Search:");

        convertBtn.setBackground(new java.awt.Color(23, 23, 23));
        convertBtn.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        convertBtn.setForeground(new java.awt.Color(208, 158, 57));
        convertBtn.setText("Convert");
        convertBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertBtnActionPerformed(evt);
            }
        });

        convertBox.setBackground(new java.awt.Color(23, 23, 23));
        convertBox.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        convertBox.setForeground(new java.awt.Color(208, 158, 57));

        jPanel2.setBackground(new java.awt.Color(208, 158, 57));

        jPanel3.setBackground(new java.awt.Color(23, 23, 23));

        progressBar.setBackground(new java.awt.Color(204, 204, 204));
        progressBar.setForeground(new java.awt.Color(255, 255, 255));

        rewindBtn.setText("jButton3");
        rewindBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rewindBtnActionPerformed(evt);
            }
        });

        fastForwardBtn.setText("jButton3");
        fastForwardBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fastForwardBtnActionPerformed(evt);
            }
        });

        playPauseBtn.setText("jButton3");
        playPauseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playPauseBtnActionPerformed(evt);
            }
        });

        maxTimeDisp.setForeground(new java.awt.Color(208, 158, 57));
        maxTimeDisp.setText("--.--");

        jLabel3.setForeground(new java.awt.Color(208, 158, 57));
        jLabel3.setText("0:00");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(246, 246, 246)
                        .addComponent(rewindBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 701, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(maxTimeDisp)
                                .addGap(26, 26, 26)))
                        .addComponent(fastForwardBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(657, 657, 657)
                        .addComponent(playPauseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fastForwardBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rewindBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(maxTimeDisp)
                            .addComponent(jLabel3))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playPauseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(152, 72, 64));

        currentUserDisp.setFont(new java.awt.Font("Arial Unicode MS", 0, 24));
        currentUserDisp.setForeground(new java.awt.Color(208, 158, 57));
        //Set logged in user
        currentUser = login.getCurrentUser();
        currentUserDisp.setText(currentUser);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(currentUserDisp)
                .addGap(14, 14, 14))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(currentUserDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        delBtn.setBackground(new java.awt.Color(23, 23, 23));
        delBtn.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        delBtn.setForeground(new java.awt.Color(208, 158, 57));
        delBtn.setText("Delete");
        delBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delBtnActionPerformed(evt);
            }
        });

        logOutBtn.setBackground(new java.awt.Color(23, 23, 23));
        logOutBtn.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        logOutBtn.setForeground(new java.awt.Color(208, 158, 57));
        logOutBtn.setText("Log Out");
        logOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutBtnActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/hutaoyao.gif"))); // NOI18N

        songDisplay.setModel(model);
        songDisplay.getTableHeader().setReorderingAllowed(false);
        songDisplay.setRowHeight(40);
        songDisplay.setShowGrid(true);
        jScrollPane1.setViewportView(songDisplay);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/hutaoyao.gif"))); // NOI18N

        jLayeredPane1.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 887, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 238, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(101, 101, 101))
        );

        sortBtn.setBackground(new java.awt.Color(23, 23, 23));
        sortBtn.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        sortBtn.setForeground(new java.awt.Color(208, 158, 57));
        sortBtn.setText("Sort");
        sortBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(currentUserDisp1)
                        .addGap(170, 170, 170))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(convertBox, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(delBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(logOutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(folderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(convertBtn)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(345, 345, 345)
                                .addComponent(currentUserDisp2)
                                .addGap(100, 100, 100)
                                .addComponent(currentUserDisp4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(currentUserDisp3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sortBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sortBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLayeredPane1)))
                                .addContainerGap())))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(currentUserDisp1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(currentUserDisp2)
                        .addComponent(currentUserDisp4)
                        .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logOutBtn)
                    .addComponent(currentUserDisp3)
                    .addComponent(sortBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sortBtn))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(fileBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(folderBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(convertBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(convertBtn)
                        .addGap(29, 29, 29)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Initialize timer
    private void initTimer() {
        timer = new javax.swing.Timer(1, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //If playing, set the progress bar as the audio progresses
                if (isPlaying) {
                    int value = (int)mediaPlayer.getCurrentTime().toSeconds();
                    progressBar.setValue(value);
                    //When song ends
                    if(mediaPlayer.getCurrentTime().equals(mediaPlayer.getTotalDuration())){
                        pauseMusic();
                        //get index of next song
                        int nextSong = songDisplay.getSelectedRow()+1;
                        //if current song is at the end of the list, set to top.
                        if (nextSong == songDisplay.getRowCount()){
                            nextSong = 0;
                        }
                        //Select the table
                        songDisplay.getSelectionModel().setSelectionInterval(nextSong, nextSong);
                        //Play the music
                        playMusic(setMusic(nextSong));
                    }
                }
            }
        });
    }
    //Log out method
    private void logOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutBtnActionPerformed
        // TODO add your handling code here:
        if (mediaPlayer!=null){
            pauseMusic();
        }
        //instance the other frame
        login loginFrame = new login();
        model.setRowCount(0);
        this.dispose();
        login.resetCurrentUser();//Set curent user to null
        loginFrame.setVisible(true);
    }//GEN-LAST:event_logOutBtnActionPerformed
    //when import file btn is pressed
    private void fileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBtnActionPerformed
        // TODO add your handling code here:
        JFileChooser songChooser = new JFileChooser("./");
        //Allowed file extensions
        FileNameExtensionFilter audioFilter = new FileNameExtensionFilter("Audio Files", "wav", "mp3", "flac", "ogg", "aac");
        songChooser.setFileFilter(audioFilter);
        int result = songChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected file
            java.io.File selectedFile = songChooser.getSelectedFile();

            // Read the file using try-with-resources to automatically close the resources
            Path path = Paths.get(selectedFile.getAbsolutePath());
            importMusic(path);
        }
    }//GEN-LAST:event_fileBtnActionPerformed
    //Import folder
    private void folderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_folderBtnActionPerformed
        // TODO add your handling code here:
        // Create a file chooser
        JFileChooser folderChooser = new JFileChooser("./");
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Show the file chooser dialog and get the selected folder
        int result = folderChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected folder
            java.io.File selectedFolder = folderChooser.getSelectedFile();
            importFolder(selectedFolder);

        }
    }//GEN-LAST:event_folderBtnActionPerformed
    //Import folder method
    private void importFolder(java.io.File selectedFolder){
            // Set a file filter for common audio file extensions
            FileNameExtensionFilter audioFilter = new FileNameExtensionFilter("Audio Files", "wav", "mp3", "flac", "ogg", "aac");

            // Read all audio files from the selected folder
            try {
                Files.walk(selectedFolder.toPath())
                        .filter(Files::isRegularFile)
                        .filter(path -> audioFilter.accept(path.toFile()))
                        //For each file in folder, run importMusic method
                        .forEach(path -> {importMusic(path);});
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    //set the playing music
    private String setMusic(int selectedRow){
        String title = songDisplay.getValueAt(selectedRow, 0).toString();
        String artist = songDisplay.getValueAt(selectedRow, 1).toString();
        String duration = songDisplay.getValueAt(selectedRow, 2).toString();
        String musicFile = "";
        for (Music music:musicList){
            if(music.getArtist().equals(artist)&&music.getMinutes().equals(duration)&&music.getTitle().equals(title)){
                musicFile = music.getPath();
                break;
            }
        }
        return musicFile;
    } 
    private void playPauseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playPauseBtnActionPerformed
        // TODO add your handling code here:
        //get selected song
        int selectedRow = songDisplay.getSelectedRow();
        //-1 means nothing is selected, return
        if (selectedRow == -1){
            return;
        }
        //get the file that will be played
        String musicFile = setMusic(selectedRow);
        //Play, switch songs, resume, and pause depending on when the button is clicked
        if(isPlaying==false&&playingMusic!=musicFile){
            playMusic(musicFile);
        }
        else if(isPlaying==true&&playingMusic!=musicFile){
            pauseMusic();
            playMusic(musicFile);
        }
        else if(isPlaying==false){
            resumeMusic();
        }
        else{
            pauseMusic();
        }
    }//GEN-LAST:event_playPauseBtnActionPerformed
    
    private void fastForwardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fastForwardBtnActionPerformed
        // TODO add your handling code here:
        if (playingMusic==null){
            return;
        }
        //The end of the song
        Duration end = new Duration(mediaPlayer.getTotalDuration().toMillis());
        //Where the song currently is
        Duration current = new Duration(mediaPlayer.getCurrentTime().toMillis());
        //If the song is less than 15 seconds from its end when this button is pressed, simply go to the end of the song.
        if(end.toMillis()-current.toMillis()<15000){
            mediaPlayer.seek(end);
            return;
        }
        //otherwise, add 15 seconds
        Duration time = current.add(new Duration(15000));
        mediaPlayer.seek(time);
        if (isPlaying==false){
            resumeMusic();
        }
    }//GEN-LAST:event_fastForwardBtnActionPerformed

    private void rewindBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rewindBtnActionPerformed
        // TODO add your handling code here:
        if (playingMusic==null){
            return;
        }
        //Start of the song is always 0
        Duration start = new Duration(0);
        Duration current = new Duration(mediaPlayer.getCurrentTime().toMillis());
        //If this song is less than 15 sec from the start, jump straight to the start
        if(current.toMillis()-start.toMillis()<15000){
            mediaPlayer.seek(start);
            return;
        }
        //Otherwise, go back 15 seconds
        Duration time = current.subtract(new Duration(15000));
        mediaPlayer.seek(time);
        if (isPlaying==false){
            resumeMusic();
        }
    }//GEN-LAST:event_rewindBtnActionPerformed

    private void convertBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertBtnActionPerformed
        // TODO add your handling code here:
        //Grab the url and write it into a json file for the python program to read
        String url = convertBox.getText();
        convertBox.setText("");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        try(FileWriter writer = new FileWriter("convertYoutubeWav/javaInp.json")){
            writer.write(jsonObject.toJSONString());
            writer.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        //Run the python code to convert this into a wav file
        try {
            File pythonScript = new File("./convertYoutubeWav/main.py");
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript.toString()).inheritIO();

            Process process = processBuilder.start();
            process.waitFor();
            //read the path of the generated wav file
            JSONParser parser = new JSONParser();
            try(FileReader reader = new FileReader("convertPath.json")){
                Object obj = parser.parse(reader);
                JSONObject pathObj = (JSONObject) obj;
                String path = pathObj.get("Path").toString();
                Path newPath = Paths.get(path);
                //import it into playlist
                importMusic(newPath);
                
            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            catch(ParseException e){
                e.printStackTrace();
            }
        }
        catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_convertBtnActionPerformed

    private void delBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delBtnActionPerformed
        // TODO add your handling code here:
        //get the selected row
        int selectedRow = songDisplay.getSelectedRow();
        String title = songDisplay.getValueAt(selectedRow, 0).toString();
        String artist = songDisplay.getValueAt(selectedRow, 1).toString();
        String duration = songDisplay.getValueAt(selectedRow, 2).toString();
        //find and remove the targetted song
        for (Music music:musicList){
            if(music.getArtist().equals(artist)&&music.getMinutes().equals(duration)&&music.getTitle().equals(title)){
                File deleteFile = new File(music.getPath());
                deleteFile.delete();
                musicList.remove(music);
                break;
            }
        }
        //remove row
        model.removeRow(selectedRow);
    }//GEN-LAST:event_delBtnActionPerformed

    private void sortBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortBtnActionPerformed
        // TODO add your handling code here:
        //get what the user intends to sort by
        String sortBy = sortBox.getSelectedItem().toString();
        //initialize sorted list
        ArrayList<Music> sortedList;
        //clear the table
        model.setRowCount(0);
        switch (sortBy){
            case "Title":
                //quicksort the unsorted list
                sortedList = quickSort.quickSortTitle(musicList);
                //recreate table based on sorted list
                for (Music music:sortedList){
                    model.addRow(new Object[]{music.getTitle(), music.getArtist(), music.getMinutes()});
                }
                break;
            case "Artist":
                //Since artist is the only field that can contain no information, bubblesort is used instead of quicksort for its stability
                sortedList = bubbleSort.bubbleArtist(musicList);
                for (Music music:musicList){
                    model.addRow(new Object[]{music.getTitle(), music.getArtist(), music.getMinutes()});
                }
                break;
            case "Duration":
                //quicksort based on duration
                sortedList = quickSort.quickSortDuration(musicList);
                for (Music music:sortedList){
                    model.addRow(new Object[]{music.getTitle(), music.getArtist(), music.getMinutes()});
                }
                break;
        }
    }//GEN-LAST:event_sortBtnActionPerformed
    //When things are typed into the search box
    private void searchBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBoxKeyTyped
        // TODO add your handling code here:
        //the table sorter will be used to search and only display the data containing the inputted text
        TableRowSorter<DefaultTableModel> songSorter = new TableRowSorter<>(model);
        songDisplay.setRowSorter(songSorter);
        //set rowfilter to grab all things containing input text, case insensitive
        RowFilter<DefaultTableModel, Object> caseInsensitiveFilter = RowFilter.regexFilter("(?i)" + searchBox.getText());
        songSorter.setRowFilter(caseInsensitiveFilter);
    }//GEN-LAST:event_searchBoxKeyTyped
    //method for loading music
    private void playMusic(String musicFile) {
        PlatformImpl.startup(() -> {
            //set song
            sound = new Media(new File(musicFile).toURI().toString());
            //set the music that is playing
            playingMusic = musicFile;
            //Set mediaplayer
            mediaPlayer = new MediaPlayer(sound);
            //once media player is ready, change the progress bar to match the values of the song
            mediaPlayer.setOnReady(() -> {
                int max = (int)sound.getDuration().toSeconds();
                progressBar.setMaximum(max);
                maxTimeDisp.setText(convertMin(max));
            });
            //Play music
            mediaPlayer.play();
            isPlaying=true;
            timer.start();
        });
    }
    //pause music
    private void pauseMusic(){
        mediaPlayer.pause();
        isPlaying=false;
        timer.stop();
    }
    //resume music
    private void resumeMusic(){
        mediaPlayer.play();
        isPlaying=true;
        timer.start();
    }
    //import audio files
    private void importMusic(Path filePath){
        int folderCnt = new File("data/"+currentUser+"/music").listFiles().length;
        Path destination = Path.of("data/"+currentUser+"/music/"+filePath.getFileName());
        try{
            //move the file from its original location to where the user's music folder is
            Files.move(filePath, destination, StandardCopyOption.REPLACE_EXISTING);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        //if the number of files in the folder didn't change, it means the user put in a file that already existed before. Stop the method
        int folderCntAftr = new File("data/"+currentUser+"/music").listFiles().length;
        if (folderCnt==folderCntAftr&&folderCnt==musicList.size()){
            return;
        }
        try {
            //Write the path into a json file for another python script to read and return metadata
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Path", destination.toString());
            try(FileWriter writer = new FileWriter("readMetadata/javaInput.json")){
                writer.write(jsonObject.toJSONString());
                writer.flush();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            //run python script
            File pythonScript = new File("./readMetadata/main.py");
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript.toString()).inheritIO();

            Process process = processBuilder.start();
            process.waitFor();
            JSONParser parser = new JSONParser();
            //read metadata from python's output in the json file
            try(FileReader reader = new FileReader("pyOut.json")){
                Object obj = parser.parse(reader);
                JSONObject musicObj = (JSONObject) obj;
                Object title = musicObj.get("title");
                if (title==null){
                    title = filePath.getFileName().toString();
                }
                Object artist = musicObj.get("artist");
                if (artist==null){
                    artist = "none";
                }
                Double duration = Double.parseDouble(musicObj.get("duration").toString());
                String path = destination.toString();
                //Create the music object and add into arraylist
                Music music = new Music(title.toString(), artist.toString(), duration, path);
                model.addRow(new Object[]{music.getTitle(), music.getArtist(), music.getMinutes()});
                musicList.add(music);
            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            catch(ParseException e){
                e.printStackTrace();
            }
        }
        catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
    //Convert to minutes for display purposes
    private String convertMin (int max){
        int min = (int)max/60;
        int seconds = max-(min*60);
        String secFormatted;
        //Add a 0 to the front if the seconds is one digit
        if (String.valueOf(seconds).length()<2){
            secFormatted = "0"+String.valueOf(seconds);
        }
        else{
            secFormatted = String.valueOf(seconds);
        }
        String maxInMin = String.valueOf(min)+":"+ secFormatted;
        return maxInMin;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new musicMenaceV2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField convertBox;
    private javax.swing.JButton convertBtn;
    private javax.swing.JLabel currentUserDisp;
    private javax.swing.JLabel currentUserDisp1;
    private javax.swing.JLabel currentUserDisp2;
    private javax.swing.JLabel currentUserDisp3;
    private javax.swing.JLabel currentUserDisp4;
    private javax.swing.JButton delBtn;
    private javax.swing.JButton fastForwardBtn;
    private javax.swing.JButton fileBtn;
    private javax.swing.JButton folderBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logOutBtn;
    private javax.swing.JLabel maxTimeDisp;
    private javax.swing.JButton playPauseBtn;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton rewindBtn;
    private javax.swing.JTextField searchBox;
    private javax.swing.JTable songDisplay;
    private javax.swing.JComboBox<String> sortBox;
    private javax.swing.JButton sortBtn;
    // End of variables declaration//GEN-END:variables
}
