package main;

import java.sql.*;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;

/**
 *
 * @author Crumbz
 */

public class Main extends javax.swing.JFrame {

    CardLayout cardLayout;
    public Main() throws ClassNotFoundException {
        initComponents();
        cardLayout = (CardLayout)(pnlCard.getLayout());
        connect();
        loadDropdown(lodgerNumDropdown, "lodgerNum", "lodger_tbl");
        loadDropdown(lodgerNumDropdown1, "lodgerNum", "lodger_tbl");
        loadDropdown(roomNumDropdown, "roomNum", "room_tbl");
        loadDropdown(roomNumDropdown1, "roomNum", "room_tbl");
        fetchTable(lodgerTbl, "lodger_tbl");
        fetchTable(roomTbl, "room_tbl");
        fetchTable(regTbl, "reg_tbl");
        loadDropdown(regIdDropdown, "regID", "reg_tbl");
    }
    
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    public void populate_db() throws SQLException, ClassNotFoundException {
        try {
            Statement stmt = con.createStatement();
            String checkLodgerTbl = "SELECT COUNT(*) FROM lodger_tbl";
            ResultSet rs = stmt.executeQuery(checkLodgerTbl);
            rs.next();
            int lodgerCount = rs.getInt(1);

            String checkRoomTbl = "SELECT COUNT(*) FROM room_tbl";
            rs = stmt.executeQuery(checkRoomTbl);
            rs.next();
            int roomCount = rs.getInt(1);

            String checkRegTbl = "SELECT COUNT(*) FROM reg_tbl";
            rs = stmt.executeQuery(checkRegTbl);
            rs.next();
            int regCount = rs.getInt(1);

            if (lodgerCount == 0 && roomCount == 0 && regCount == 0) {
                System.out.println("Inserting data into tables...");

                String insertLodger = "INSERT INTO lodger_tbl (lodgerNum,lodgerLastName,lodgerFirstName,lodgerCity,lodgerStreet,lodgerEmail,lodgerContactNum) VALUES (100001,'Smith','John','New York City','Main Street','john.smith@example.com','5551234567'), (100002,'Johnson','Emily','Los Angeles','Maple Ave','emily.johnson@example.com','5559876543'), (100003,'Williams','Michael','Chicago','Oak Road','michael.williams@example.com','5557891234'), (100004,'Jones','Sophia','Houston','Pine St','sophia.jones@example.com','5559012345'), (100005,'Brown','David','Phoenix','Cedar Ln','david.brown@example.com','5556789012'), (100006,'Davis','Olivia','Philadelphia','Elm Ave','olivia.davis@example.com','5553456789'), (100007,'Miller','Daniel','San Antonio','Maple Blvd','daniel.miller@example.com','5551234509')";
                PreparedStatement preparedStatementLodger = con.prepareStatement(insertLodger);
                preparedStatementLodger.execute();

                String insertRoom = "INSERT INTO room_tbl (roomNum,roomTier,roomPax,roomPrice) VALUES (201,'Standard',4,3000.00), (202,'Standard',4,3000.00), (203,'Standard',4,3000.00), (204,'Deluxe',6,4500.00), (205,'Deluxe',6,4500.00), (206,'Deluxe',6,4500.00), (207,'Executive',10,8000.00), (208,'Executive',10,8000.00), (209,'Presidential',12,12000.00), (210,'Presidential',12,12000.00)";
                PreparedStatement preparedStatementRoom = con.prepareStatement(insertRoom);
                preparedStatementRoom.execute();

                String insertReg = "INSERT INTO reg_tbl (regID,days,checkIn,checkOut,totalAmount,mop,lodgerNum,roomNum) VALUES(1234567,2,'2024-01-10','2024-01-12',9000.00,'Cash',100001,204),(2345678,3,'2024-01-16','2024-01-19',9000.00,'Cash',100001,201),(3456789,5,'2024-01-20','2024-01-25',22500.00,'Credit Card',100001,205),(4567890,4,'2024-01-24','2024-01-28',48000.00,'Credit Card',100002,210),(5678901,2,'2024-01-20','2024-01-22',9000.00,'Online',100002,205),(6789012,1,'2024-01-20','2024-01-21',8000.00,'Credit Card',100003,208),(7890123,1,'2024-01-01','2024-01-02',12000.00,'Credit Card',100004,210),(8901234,2,'2024-01-29','2024-01-31',6000.00,'Cash',100004,201),(9012345,1,'2024-01-03','2024-01-04',3000.00,'Online',100005,202),(1234509,2,'2024-01-14','2024-01-16',6000.00,'Online',100005,202),(2345610,1,'2024-01-24','2024-01-25',4500.00,'Cash',100005,206),(3456711,2,'2024-01-06','2024-01-08',24000.00,'Credit Card',100005,209),(4567812,5,'2024-01-10','2024-01-15',15000.00,'Online',100006,203),(5678913,1,'2024-01-17','2024-01-18',8000.00,'Online',100006,207),(6789014,1,'2024-01-26','2024-01-27',8000.00,'Online',100006,208),(7890115,2,'2024-01-24','2024-01-26',24000.00,'Credit Card',100007,209)";
                PreparedStatement preparedStatementReg = con.prepareStatement(insertReg);

                preparedStatementReg.execute();

                System.out.println("Data insertion completed.");
            } else {
                System.out.println("Tables already populated. Skipping data insertion.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void connect() throws ClassNotFoundException {
      Class.forName("org.h2.Driver");
      final String url = "jdbc:h2:./bnbdb";
      final String username = "sa";
      final String password = "";
      try {
        con = DriverManager.getConnection(url, username, password);

        Statement stmt = con.createStatement();
        String createLodgerTbl = "CREATE TABLE IF NOT EXISTS lodger_tbl (lodgerNum INT NOT NULL, lodgerLastName varchar(25) NOT NULL, lodgerFirstName varchar(30) NOT NULL, lodgerCity varchar(50) NOT NULL, lodgerStreet varchar(50) NOT NULL, lodgerEmail varchar(30) NOT NULL, lodgerContactNum varchar(11) NOT NULL, PRIMARY KEY (lodgerNum))";
        String createRoomTbl = "CREATE TABLE IF NOT EXISTS room_tbl (roomNum INT NOT NULL , roomTier VARCHAR(50) NOT NULL , roomPax INT NOT NULL , roomPrice DECIMAL NOT NULL , PRIMARY KEY (roomNum))";
        String createRegTbl = "CREATE TABLE IF NOT EXISTS reg_tbl (regID INT NOT NULL , days INT NOT NULL , checkIn DATE NOT NULL , checkOut DATE NOT NULL , totalAmount DECIMAL NOT NULL , mop VARCHAR(50) NOT NULL , lodgerNum INT NOT NULL , roomNum INT NOT NULL , PRIMARY KEY (regID))";
        String alterRegTbl = "ALTER TABLE reg_tbl ADD CONSTRAINT lodgerNum FOREIGN KEY (lodgerNum) REFERENCES lodger_tbl(lodgerNum) ON DELETE RESTRICT ON UPDATE RESTRICT; ALTER TABLE reg_tbl ADD CONSTRAINT roomNum FOREIGN KEY (roomNum) REFERENCES room_tbl(roomNum) ON DELETE RESTRICT ON UPDATE RESTRICT;";
        stmt.execute(createLodgerTbl);
        stmt.execute(createRoomTbl);
        stmt.execute(createRegTbl);
        stmt.execute(alterRegTbl);
        populate_db();

      } catch (SQLSyntaxErrorException e){
        System.out.println("Constraint already there");
      } catch (SQLException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    public void loadDropdown(JComboBox<String> dropdown, String columnName, String tableName) {
        try {
            pst = con.prepareStatement("SELECT " + columnName + " FROM " + tableName);
            rs = pst.executeQuery();
            dropdown.removeAllItems();

            while (rs.next()) {
                dropdown.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fetchTable(JTable table, String tableName) {
        try {
            pst = con.prepareStatement("SELECT * FROM " + tableName);
            rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            int columnCount = rss.getColumnCount();

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector<Object> row = new Vector<>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        navbar = new javax.swing.JPanel();
        toLodgerBtn = new javax.swing.JButton();
        toRegistrationBtn = new javax.swing.JButton();
        toRoomBtn = new javax.swing.JButton();
        pnlCard = new javax.swing.JPanel();
        lodgerPanel = new javax.swing.JPanel();
        titleNavPanel = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        addLodgerBtn = new javax.swing.JButton();
        editLodgerBtn = new javax.swing.JButton();
        dltLodgerBtn = new javax.swing.JButton();
        searchLodgerBtn = new javax.swing.JButton();
        lodgerNumDropdown = new javax.swing.JComboBox<>();
        txtFldPnl = new javax.swing.JPanel();
        fnameTxtFld = new javax.swing.JTextField();
        lnameTxtFld = new javax.swing.JTextField();
        cityTxtFld = new javax.swing.JTextField();
        emailAddressTxtFld = new javax.swing.JTextField();
        contactNumTxtFld = new javax.swing.JTextField();
        streetTxtFld = new javax.swing.JTextField();
        lodgerNumTxtFld = new javax.swing.JTextField();
        tblPnl = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lodgerTbl = new javax.swing.JTable();
        registrationPanel = new javax.swing.JPanel();
        titleNavPanel1 = new javax.swing.JPanel();
        title1 = new javax.swing.JLabel();
        addRegBtn = new javax.swing.JButton();
        edtRegBtn = new javax.swing.JButton();
        dltRegBtn = new javax.swing.JButton();
        srchRegBtn = new javax.swing.JButton();
        regIdDropdown = new javax.swing.JComboBox<>();
        txtFldPnl1 = new javax.swing.JPanel();
        regIDTxtFld = new javax.swing.JTextField();
        lodgerNumDropdown1 = new javax.swing.JComboBox<>();
        roomNumDropdown1 = new javax.swing.JComboBox<>();
        staySpinner = new javax.swing.JSpinner();
        checkInTxtFld = new javax.swing.JTextField();
        checkOutTxtFld = new javax.swing.JTextField();
        totalAmountTxtFld = new javax.swing.JTextField();
        mopDropdown = new javax.swing.JComboBox<>();
        tblPnl1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        regTbl = new javax.swing.JTable();
        roomPanel = new javax.swing.JPanel();
        titleNavPanel2 = new javax.swing.JPanel();
        title2 = new javax.swing.JLabel();
        addRoomBtn = new javax.swing.JButton();
        edtRoomBtn = new javax.swing.JButton();
        dltRoomBtn = new javax.swing.JButton();
        srchRoomBtn = new javax.swing.JButton();
        roomNumDropdown = new javax.swing.JComboBox<>();
        txtFldPnl2 = new javax.swing.JPanel();
        roomNumTxtFld = new javax.swing.JTextField();
        roomTierDropdown = new javax.swing.JComboBox<>();
        roomPaxTxtFld = new javax.swing.JTextField();
        roomPriceTxtFld = new javax.swing.JTextField();
        tblPnl2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        roomTbl = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1100, 600));
        setMinimumSize(new java.awt.Dimension(1100, 600));
        setPreferredSize(new java.awt.Dimension(1100, 600));
        setResizable(false);

        navbar.setBackground(new java.awt.Color(255, 255, 255));

        toLodgerBtn.setText("Lodger");
        toLodgerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toLodgerBtnActionPerformed(evt);
            }
        });

        toRegistrationBtn.setText("Registration");
        toRegistrationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toRegistrationBtnActionPerformed(evt);
            }
        });

        toRoomBtn.setText("Room");
        toRoomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toRoomBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout navbarLayout = new javax.swing.GroupLayout(navbar);
        navbar.setLayout(navbarLayout);
        navbarLayout.setHorizontalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(toLodgerBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toRoomBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toRegistrationBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        navbarLayout.setVerticalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(toLodgerBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(toRegistrationBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(toRoomBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(navbar);

        pnlCard.setLayout(new java.awt.CardLayout());

        titleNavPanel.setBackground(new java.awt.Color(255, 255, 255));

        title.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        title.setText("Lodger");

        addLodgerBtn.setText("Add");
        addLodgerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLodgerBtnActionPerformed(evt);
            }
        });

        editLodgerBtn.setText("Edit");
        editLodgerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editLodgerBtnActionPerformed(evt);
            }
        });

        dltLodgerBtn.setText("Delete");
        dltLodgerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dltLodgerBtnActionPerformed(evt);
            }
        });

        searchLodgerBtn.setText("Search");
        searchLodgerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchLodgerBtnActionPerformed(evt);
            }
        });

        lodgerNumDropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout titleNavPanelLayout = new javax.swing.GroupLayout(titleNavPanel);
        titleNavPanel.setLayout(titleNavPanelLayout);
        titleNavPanelLayout.setHorizontalGroup(
            titleNavPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleNavPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(addLodgerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(editLodgerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(dltLodgerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(searchLodgerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 13, Short.MAX_VALUE)
                .addComponent(lodgerNumDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        titleNavPanelLayout.setVerticalGroup(
            titleNavPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleNavPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(titleNavPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(title)
                    .addComponent(addLodgerBtn)
                    .addComponent(editLodgerBtn)
                    .addComponent(dltLodgerBtn)
                    .addComponent(searchLodgerBtn)
                    .addComponent(lodgerNumDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        txtFldPnl.setBackground(new java.awt.Color(255, 255, 255));

        fnameTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("First Name"));
        fnameTxtFld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fnameTxtFldKeyPressed(evt);
            }
        });

        lnameTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("Last Name"));
        lnameTxtFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lnameTxtFldActionPerformed(evt);
            }
        });
        lnameTxtFld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lnameTxtFldKeyPressed(evt);
            }
        });

        cityTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("City"));
        cityTxtFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cityTxtFldActionPerformed(evt);
            }
        });
        cityTxtFld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cityTxtFldKeyPressed(evt);
            }
        });

        emailAddressTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("Email Address"));
        emailAddressTxtFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailAddressTxtFldActionPerformed(evt);
            }
        });

        contactNumTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("Contact Number"));
        contactNumTxtFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactNumTxtFldActionPerformed(evt);
            }
        });
        contactNumTxtFld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                contactNumTxtFldKeyPressed(evt);
            }
        });

        streetTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("Street"));
        streetTxtFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                streetTxtFldActionPerformed(evt);
            }
        });

        lodgerNumTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("LodgerNum"));
        lodgerNumTxtFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lodgerNumTxtFldActionPerformed(evt);
            }
        });
        lodgerNumTxtFld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lodgerNumTxtFldKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout txtFldPnlLayout = new javax.swing.GroupLayout(txtFldPnl);
        txtFldPnl.setLayout(txtFldPnlLayout);
        txtFldPnlLayout.setHorizontalGroup(
            txtFldPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtFldPnlLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(txtFldPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(emailAddressTxtFld, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(cityTxtFld)
                    .addComponent(fnameTxtFld)
                    .addComponent(lodgerNumTxtFld))
                .addGap(18, 18, 18)
                .addGroup(txtFldPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(txtFldPnlLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lnameTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(streetTxtFld)
                    .addComponent(contactNumTxtFld))
                .addGap(16, 16, 16))
        );
        txtFldPnlLayout.setVerticalGroup(
            txtFldPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtFldPnlLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lodgerNumTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(txtFldPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fnameTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lnameTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(txtFldPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cityTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(streetTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(txtFldPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailAddressTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactNumTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(364, Short.MAX_VALUE))
        );

        tblPnl.setBackground(new java.awt.Color(255, 255, 255));

        lodgerTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "LodgerNum", "Last Name", "First Name", "City", "Street", "Email", "Contact Num"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(lodgerTbl);

        javax.swing.GroupLayout tblPnlLayout = new javax.swing.GroupLayout(tblPnl);
        tblPnl.setLayout(tblPnlLayout);
        tblPnlLayout.setHorizontalGroup(
            tblPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tblPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                .addContainerGap())
        );
        tblPnlLayout.setVerticalGroup(
            tblPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tblPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout lodgerPanelLayout = new javax.swing.GroupLayout(lodgerPanel);
        lodgerPanel.setLayout(lodgerPanelLayout);
        lodgerPanelLayout.setHorizontalGroup(
            lodgerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lodgerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lodgerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFldPnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(titleNavPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tblPnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        lodgerPanelLayout.setVerticalGroup(
            lodgerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lodgerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lodgerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tblPnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(lodgerPanelLayout.createSequentialGroup()
                        .addComponent(titleNavPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(txtFldPnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCard.add(lodgerPanel, "lodgerCard");

        titleNavPanel1.setBackground(new java.awt.Color(255, 255, 255));

        title1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        title1.setText("Registration");

        addRegBtn.setText("Add");
        addRegBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRegBtnActionPerformed(evt);
            }
        });

        edtRegBtn.setText("Edit");
        edtRegBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtRegBtnActionPerformed(evt);
            }
        });

        dltRegBtn.setText("Delete");
        dltRegBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dltRegBtnActionPerformed(evt);
            }
        });

        srchRegBtn.setText("Search");
        srchRegBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                srchRegBtnActionPerformed(evt);
            }
        });

        regIdDropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout titleNavPanel1Layout = new javax.swing.GroupLayout(titleNavPanel1);
        titleNavPanel1.setLayout(titleNavPanel1Layout);
        titleNavPanel1Layout.setHorizontalGroup(
            titleNavPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleNavPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(title1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(addRegBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edtRegBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dltRegBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(srchRegBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regIdDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );
        titleNavPanel1Layout.setVerticalGroup(
            titleNavPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleNavPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(titleNavPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(title1)
                    .addComponent(addRegBtn)
                    .addComponent(edtRegBtn)
                    .addComponent(dltRegBtn)
                    .addComponent(srchRegBtn)
                    .addComponent(regIdDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        txtFldPnl1.setBackground(new java.awt.Color(255, 255, 255));

        regIDTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("Registration ID"));
        regIDTxtFld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                regIDTxtFldKeyPressed(evt);
            }
        });

        lodgerNumDropdown1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        lodgerNumDropdown1.setBorder(javax.swing.BorderFactory.createTitledBorder("LodgerNum"));

        roomNumDropdown1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        roomNumDropdown1.setBorder(javax.swing.BorderFactory.createTitledBorder("RoomNum"));
        roomNumDropdown1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomNumDropdown1ActionPerformed(evt);
            }
        });

        staySpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        staySpinner.setBorder(javax.swing.BorderFactory.createTitledBorder("Stay (in days)"));

        checkInTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("Check In Date (yyyy-MM-dd)"));
        checkInTxtFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkInTxtFldActionPerformed(evt);
            }
        });
        checkInTxtFld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkInTxtFldKeyPressed(evt);
            }
        });

        checkOutTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("Check Out Date (yyyy-MM-dd)"));
        checkOutTxtFld.setEnabled(false);

        totalAmountTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("Total Amount"));
        totalAmountTxtFld.setEnabled(false);

        mopDropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Credit Card", "Online" }));
        mopDropdown.setBorder(javax.swing.BorderFactory.createTitledBorder("MOP"));

        javax.swing.GroupLayout txtFldPnl1Layout = new javax.swing.GroupLayout(txtFldPnl1);
        txtFldPnl1.setLayout(txtFldPnl1Layout);
        txtFldPnl1Layout.setHorizontalGroup(
            txtFldPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtFldPnl1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(txtFldPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(txtFldPnl1Layout.createSequentialGroup()
                        .addComponent(regIDTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(roomNumDropdown1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(txtFldPnl1Layout.createSequentialGroup()
                        .addGroup(txtFldPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(checkInTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lodgerNumDropdown1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mopDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(txtFldPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(staySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkOutTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalAmountTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        txtFldPnl1Layout.setVerticalGroup(
            txtFldPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtFldPnl1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(txtFldPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(regIDTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roomNumDropdown1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(txtFldPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(staySpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(lodgerNumDropdown1))
                .addGap(18, 18, 18)
                .addGroup(txtFldPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkInTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkOutTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(txtFldPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalAmountTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mopDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        regTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "RegID", "Days", "CheckInDate", "CheckOutDate", "TotalAmount", "MOP", "LodgerNum", "RoomNum"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(regTbl);

        javax.swing.GroupLayout tblPnl1Layout = new javax.swing.GroupLayout(tblPnl1);
        tblPnl1.setLayout(tblPnl1Layout);
        tblPnl1Layout.setHorizontalGroup(
            tblPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tblPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                .addContainerGap())
        );
        tblPnl1Layout.setVerticalGroup(
            tblPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tblPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout registrationPanelLayout = new javax.swing.GroupLayout(registrationPanel);
        registrationPanel.setLayout(registrationPanelLayout);
        registrationPanelLayout.setHorizontalGroup(
            registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(titleNavPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFldPnl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tblPnl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        registrationPanelLayout.setVerticalGroup(
            registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tblPnl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(registrationPanelLayout.createSequentialGroup()
                        .addComponent(titleNavPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(txtFldPnl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCard.add(registrationPanel, "registrationCard");

        titleNavPanel2.setBackground(new java.awt.Color(255, 255, 255));

        title2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        title2.setText("Room");

        addRoomBtn.setText("Add");
        addRoomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRoomBtnActionPerformed(evt);
            }
        });

        edtRoomBtn.setText("Edit");
        edtRoomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtRoomBtnActionPerformed(evt);
            }
        });

        dltRoomBtn.setText("Delete");
        dltRoomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dltRoomBtnActionPerformed(evt);
            }
        });

        srchRoomBtn.setText("Search");
        srchRoomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                srchRoomBtnActionPerformed(evt);
            }
        });

        roomNumDropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout titleNavPanel2Layout = new javax.swing.GroupLayout(titleNavPanel2);
        titleNavPanel2.setLayout(titleNavPanel2Layout);
        titleNavPanel2Layout.setHorizontalGroup(
            titleNavPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleNavPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(title2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addRoomBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(edtRoomBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dltRoomBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(srchRoomBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(roomNumDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        titleNavPanel2Layout.setVerticalGroup(
            titleNavPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleNavPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(titleNavPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(title2)
                    .addComponent(addRoomBtn)
                    .addComponent(edtRoomBtn)
                    .addComponent(dltRoomBtn)
                    .addComponent(srchRoomBtn)
                    .addComponent(roomNumDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        txtFldPnl2.setBackground(new java.awt.Color(255, 255, 255));

        roomNumTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("Room Num"));
        roomNumTxtFld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                roomNumTxtFldKeyPressed(evt);
            }
        });

        roomTierDropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Standard", "Deluxe", "Executive", "Presidential" }));
        roomTierDropdown.setBorder(javax.swing.BorderFactory.createTitledBorder("Room Tier"));
        roomTierDropdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomTierDropdownActionPerformed(evt);
            }
        });

        roomPaxTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("Room Pax"));
        roomPaxTxtFld.setEnabled(false);

        roomPriceTxtFld.setBorder(javax.swing.BorderFactory.createTitledBorder("Room Price"));
        roomPriceTxtFld.setEnabled(false);

        javax.swing.GroupLayout txtFldPnl2Layout = new javax.swing.GroupLayout(txtFldPnl2);
        txtFldPnl2.setLayout(txtFldPnl2Layout);
        txtFldPnl2Layout.setHorizontalGroup(
            txtFldPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, txtFldPnl2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(txtFldPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(roomPaxTxtFld)
                    .addComponent(roomNumTxtFld, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addGap(69, 69, 69)
                .addGroup(txtFldPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(roomTierDropdown, 0, 201, Short.MAX_VALUE)
                    .addComponent(roomPriceTxtFld))
                .addGap(47, 47, 47))
        );
        txtFldPnl2Layout.setVerticalGroup(
            txtFldPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(txtFldPnl2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(txtFldPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(roomNumTxtFld)
                    .addComponent(roomTierDropdown))
                .addGap(47, 47, 47)
                .addGroup(txtFldPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(roomPaxTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roomPriceTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        roomTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Room Num", "Room Tier", "RoomPax", "Room Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(roomTbl);

        javax.swing.GroupLayout tblPnl2Layout = new javax.swing.GroupLayout(tblPnl2);
        tblPnl2.setLayout(tblPnl2Layout);
        tblPnl2Layout.setHorizontalGroup(
            tblPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tblPnl2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                .addContainerGap())
        );
        tblPnl2Layout.setVerticalGroup(
            tblPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tblPnl2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout roomPanelLayout = new javax.swing.GroupLayout(roomPanel);
        roomPanel.setLayout(roomPanelLayout);
        roomPanelLayout.setHorizontalGroup(
            roomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(titleNavPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFldPnl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tblPnl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        roomPanelLayout.setVerticalGroup(
            roomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tblPnl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roomPanelLayout.createSequentialGroup()
                        .addComponent(titleNavPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(txtFldPnl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCard.add(roomPanel, "roomCard");

        jSplitPane1.setRightComponent(pnlCard);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void toRoomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toRoomBtnActionPerformed
        cardLayout.show(pnlCard, "roomCard");
    }//GEN-LAST:event_toRoomBtnActionPerformed

    private void toLodgerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toLodgerBtnActionPerformed
        cardLayout.show(pnlCard, "lodgerCard");
    }//GEN-LAST:event_toLodgerBtnActionPerformed

    private void toRegistrationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toRegistrationBtnActionPerformed
        cardLayout.show(pnlCard, "registrationCard");
    }//GEN-LAST:event_toRegistrationBtnActionPerformed

    private void lnameTxtFldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lnameTxtFldActionPerformed

    }//GEN-LAST:event_lnameTxtFldActionPerformed

    private void cityTxtFldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cityTxtFldActionPerformed

    }//GEN-LAST:event_cityTxtFldActionPerformed

    private void emailAddressTxtFldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailAddressTxtFldActionPerformed

    }//GEN-LAST:event_emailAddressTxtFldActionPerformed

    private void contactNumTxtFldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactNumTxtFldActionPerformed

    }//GEN-LAST:event_contactNumTxtFldActionPerformed

    private void streetTxtFldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_streetTxtFldActionPerformed

    }//GEN-LAST:event_streetTxtFldActionPerformed

    private void addRegBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRegBtnActionPerformed
        try {
            if (regIDTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "RegID is required", "", JOptionPane.ERROR_MESSAGE);
            } else if (checkInTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Check in date is required", "", JOptionPane.ERROR_MESSAGE);
            } else {
                LocalDate checkInDate;
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String mop = mopDropdown.getSelectedItem().toString();
                double roomPrice = 0;
                int regNum = Integer.parseInt(regIDTxtFld.getText());
                int roomNum = Integer.parseInt(roomNumDropdown1.getSelectedItem().toString());
                int lodgerNum = Integer.parseInt(lodgerNumDropdown1.getSelectedItem().toString());

                String date = checkInTxtFld.getText();
                checkInDate = LocalDate.parse(date, df);

                if (checkInDate.isBefore(LocalDate.now())){
                    throw new DateIsInThePastException();
                }
                int stay = (int)staySpinner.getValue();
                LocalDate checkOutDate = checkInDate.plusDays(stay);

                pst = con.prepareStatement("SELECT roomPrice FROM room_tbl WHERE roomNum=?");
                pst.setInt(1, roomNum);

                rs = pst.executeQuery();

                while (rs.next()){
                    roomPrice = rs.getDouble("roomPrice");
                }
                double totalAmount = roomPrice * stay;

                pst = con.prepareStatement("INSERT INTO reg_tbl (regID, days, checkIn, checkOut, totalAmount, mop, lodgerNum, roomNum) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                pst.setInt(1, regNum);
                pst.setInt(2, stay);
                pst.setDate(3, java.sql.Date.valueOf(checkInDate));
                pst.setDate(4, java.sql.Date.valueOf(checkOutDate));
                pst.setDouble(5, totalAmount);
                pst.setString(6, mop);
                pst.setInt(7, lodgerNum);
                pst.setInt(8, roomNum);

                int k = pst.executeUpdate();

                if (k == 1){
                    JOptionPane.showMessageDialog(this, "Record added successfully!");
                    regIDTxtFld.setText("");
                    checkInTxtFld.setText("");
                    staySpinner.setValue(1);
                    regIDTxtFld.requestFocus();
                    loadDropdown(regIdDropdown, "regID", "reg_tbl");
                    loadDropdown(roomNumDropdown1, "roomNum", "room_tbl");
                    loadDropdown(lodgerNumDropdown1, "lodgerNum", "lodger_tbl");
                    fetchTable(regTbl, "reg_tbl");

                } else {
                    JOptionPane.showMessageDialog(this, "Record failed to save!");
                }
            }
        } catch(JdbcSQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, " Unique index or primary key violation.");
            regIDTxtFld.setText("");
            checkInTxtFld.setText("");
            staySpinner.setValue(1);
            regIDTxtFld.requestFocus();
        }catch (DateTimeParseException e){
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
            checkInTxtFld.setText("");
        } catch (NullPointerException e){
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
            checkInTxtFld.setText("");
        } catch (DateIsInThePastException e){
            JOptionPane.showMessageDialog(this, "Invalid date format. Date is in the past!");
            checkInTxtFld.setText("");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }//GEN-LAST:event_addRegBtnActionPerformed

    private void addLodgerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLodgerBtnActionPerformed
        // TODO add your handling code here:
        final String REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        try {
            if (lodgerNumTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "LodgerNum is required", "", JOptionPane.ERROR_MESSAGE);
            } else if (fnameTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "First Name is required!", "", JOptionPane.ERROR_MESSAGE);
            } else if (lnameTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Last Name is required!", "", JOptionPane.ERROR_MESSAGE);
            } else if (cityTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "City is required!", "", JOptionPane.ERROR_MESSAGE);
            } else if (streetTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Street is required!", "", JOptionPane.ERROR_MESSAGE);
            } else if (emailAddressTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Email is required!", "", JOptionPane.ERROR_MESSAGE);
            }  else if (!emailAddressTxtFld.getText().matches(REGEX)){
                JOptionPane.showMessageDialog(this, "Invalid email input. Please follow OWASP email format!", "", JOptionPane.ERROR_MESSAGE);
            } else if (contactNumTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Contact Number is required!", "", JOptionPane.ERROR_MESSAGE);
            } else if (!contactNumTxtFld.getText().matches("^(09)\\d{9}$")) {
                JOptionPane.showMessageDialog(this, "Contact Number should start with 09 and not be greater than 11 characters!", "", JOptionPane.ERROR_MESSAGE);
                contactNumTxtFld.setText("");
                contactNumTxtFld.requestFocus();
            } else {
                int lodgerNum = Integer.parseInt(lodgerNumTxtFld.getText());
                String lastName = lnameTxtFld.getText();
                String firstName = fnameTxtFld.getText();
                String city = cityTxtFld.getText();
                String street = streetTxtFld.getText();
                String email = emailAddressTxtFld.getText();
                String contactNum = contactNumTxtFld.getText();
//                String modeOfPayment = mopDropdown.getSelectedItem().toString();

                pst = con.prepareStatement("INSERT INTO lodger_tbl (lodgerNum, lodgerLastName, lodgerFirstName, lodgerCity, lodgerStreet, lodgerEmail, lodgerContactNum)VALUES(?, ?, ?, ?, ?, ?, ?)");
                pst.setInt(1, lodgerNum);
                pst.setString(2, lastName);
                pst.setString(3, firstName);
                pst.setString(4, city);
                pst.setString(5, street);
                pst.setString(6, email);
                pst.setString(7, contactNum);

                int k = pst.executeUpdate();

                if (k == 1){
                    JOptionPane.showMessageDialog(this, "Record added successfully!");
                    lodgerNumTxtFld.setText("");
                    lnameTxtFld.setText("");
                    fnameTxtFld.setText("");
                    cityTxtFld.setText("");
                    streetTxtFld.setText("");
                    emailAddressTxtFld.setText("");
                    contactNumTxtFld.setText("");
                    loadDropdown(lodgerNumDropdown, "lodgerNum", "lodger_tbl");
                    loadDropdown(lodgerNumDropdown1, "lodgerNum", "lodger_tbl");
                    fetchTable(lodgerTbl, "lodger_tbl");
                    fetchTable(regTbl, "reg_tbl");
                } else {
                    JOptionPane.showMessageDialog(this, "Record failed to save!", "", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch(JdbcSQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, " Unique index or primary key violation.");
            lodgerNumTxtFld.setText("");
            lnameTxtFld.setText("");
            fnameTxtFld.setText("");
            cityTxtFld.setText("");
            streetTxtFld.setText("");
            emailAddressTxtFld.setText("");
            contactNumTxtFld.setText("");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addLodgerBtnActionPerformed

    private void editLodgerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editLodgerBtnActionPerformed
        // TODO add your handling code here:
        final String REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        try {
            if (!lodgerNumTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "LodgerNum can't be edited!", "", JOptionPane.ERROR_MESSAGE);
                lodgerNumTxtFld.setText("");
            } else if (fnameTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "First Name is required!", "", JOptionPane.ERROR_MESSAGE);
            } else if (lnameTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Last Name is required!", "", JOptionPane.ERROR_MESSAGE);
            } else if (cityTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "City is required!", "", JOptionPane.ERROR_MESSAGE);
            } else if (streetTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Street is required!", "", JOptionPane.ERROR_MESSAGE);
            } else if (emailAddressTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Email is required!", "", JOptionPane.ERROR_MESSAGE);
            } else if (!emailAddressTxtFld.getText().matches(REGEX)){
                JOptionPane.showMessageDialog(this, "Invalid email input. Please follow OWASP email format!", "", JOptionPane.ERROR_MESSAGE);
            } else if (contactNumTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Contact Number is required!", "", JOptionPane.ERROR_MESSAGE);
            } else if (!contactNumTxtFld.getText().matches("^(09)\\d{9}$")) {
                JOptionPane.showMessageDialog(this, "Contact Number should start with 09 and not be greater than 11 characters!", "", JOptionPane.ERROR_MESSAGE);
                contactNumTxtFld.setText("");
                contactNumTxtFld.requestFocus();
            } else {
                String lastName = lnameTxtFld.getText();
                String firstName = fnameTxtFld.getText();
                String city = cityTxtFld.getText();
                String street = streetTxtFld.getText();
                String email = emailAddressTxtFld.getText();
                String contactNum = contactNumTxtFld.getText();
    //            String modeOfPayment = mopDropdown.getSelectedItem().toString();
                int lodgerNum = Integer.parseInt(lodgerNumDropdown.getSelectedItem().toString());

                pst = con.prepareStatement("UPDATE lodger_tbl SET lodgerLastName=?, lodgerFirstName=?, lodgerCity=?, lodgerStreet=?, lodgerEmail=?, lodgerContactNum=? WHERE lodgerNum=?");
                pst.setString(1, lastName);
                pst.setString(2, firstName);
                pst.setString(3, city);
                pst.setString(4, street);
                pst.setString(5, email);
                pst.setString(6, contactNum);
    //            pst.setString(7, modeOfPayment);
                pst.setInt(7, lodgerNum);

                int k = pst.executeUpdate();

                if (k == 1){
                    JOptionPane.showMessageDialog(this, "Record updated successfully!");
                    lnameTxtFld.setText("");
                    fnameTxtFld.setText("");
                    cityTxtFld.setText("");
                    streetTxtFld.setText("");
                    emailAddressTxtFld.setText("");
                    contactNumTxtFld.setText("");
                    fnameTxtFld.requestFocus();
                    loadDropdown(lodgerNumDropdown, "lodgerNum", "lodger_tbl");
                    loadDropdown(lodgerNumDropdown1, "lodgerNum", "lodger_tbl");
                    fetchTable(lodgerTbl, "lodger_tbl");
                    fetchTable(regTbl, "reg_tbl");
                } else {
                    JOptionPane.showMessageDialog(this, "Record failed to delete!");
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_editLodgerBtnActionPerformed

    private void dltLodgerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dltLodgerBtnActionPerformed
        try {
            // TODO add your handling code here:
            String lodgerNum = lodgerNumDropdown.getSelectedItem().toString();
            pst = con.prepareStatement("DELETE FROM lodger_tbl WHERE lodgerNum=?");
            pst.setString(1, lodgerNum);
            
            int k = pst.executeUpdate();
            
            if (k == 1){
                JOptionPane.showMessageDialog(this, "Record deleted successfully!");
                lnameTxtFld.setText("");
                fnameTxtFld.setText("");
                cityTxtFld.setText("");
                streetTxtFld.setText("");
                emailAddressTxtFld.setText("");
                contactNumTxtFld.setText("");
                fnameTxtFld.requestFocus();
                loadDropdown(lodgerNumDropdown, "lodgerNum", "lodger_tbl");
                loadDropdown(lodgerNumDropdown1, "lodgerNum", "lodger_tbl");
                fetchTable(lodgerTbl, "lodger_tbl");
                fetchTable(regTbl, "reg_tbl");


            }
        } catch (JdbcSQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(this, "Cannot delete or update a parent row due to constraints");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_dltLodgerBtnActionPerformed

    private void searchLodgerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchLodgerBtnActionPerformed
        try {
            int lodgerNum = Integer.parseInt(lodgerNumDropdown.getSelectedItem().toString());
            pst = con.prepareStatement("SELECT * FROM lodger_tbl WHERE lodgerNum=?");
            pst.setInt(1, lodgerNum);
            rs = pst.executeQuery();
            
            if (rs.next() == true){
                lnameTxtFld.setText(rs.getString(2));
                fnameTxtFld.setText(rs.getString(3));
                cityTxtFld.setText(rs.getString(4));
                streetTxtFld.setText(rs.getString(5));
                emailAddressTxtFld.setText(rs.getString(6));
                contactNumTxtFld.setText(rs.getString(7));
//                mopDropdown.setSelectedItem(rs.getString(8));
            } else {
                JOptionPane.showMessageDialog(this, "No record found!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_searchLodgerBtnActionPerformed

    private void lodgerNumTxtFldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lodgerNumTxtFldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lodgerNumTxtFldActionPerformed

    private void addRoomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRoomBtnActionPerformed
        try {
            if (roomNumTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "RoomNum is required", "", JOptionPane.ERROR_MESSAGE);
            } else {
                int roomPax = 0 ;
                double roomPrice = 0;

                int roomNum = Integer.parseInt(roomNumTxtFld.getText());
                String roomTier = (String) roomTierDropdown.getSelectedItem();

                switch (roomTier){
                    case "Standard" -> {
                        roomPax = 4;
                        roomPrice = 3000.00;
                        break;
                    }
                    case "Deluxe" -> {
                        roomPax = 6;
                        roomPrice = 4500.00;
                        break;
                    }
                    case "Executive" -> {
                        roomPax = 10;
                        roomPrice = 8000.00;
                        break;
                    }
                    case "Presidential" -> {
                        roomPax = 12;
                        roomPrice = 12000.00;
                        break;
                    }
                }
                pst = con.prepareStatement("INSERT INTO room_tbl (roomNum, roomTier, roomPax, roomPrice)VALUES(?, ?, ?, ?)");
                pst.setInt(1, roomNum);
                pst.setString(2, roomTier);
                pst.setInt(3, roomPax);
                pst.setDouble(4, roomPrice);

                int k = pst.executeUpdate();

                if (k == 1){
                    JOptionPane.showMessageDialog(this, "Record added successfully!");
                    roomNumTxtFld.setText("");
                    loadDropdown(roomNumDropdown, "roomNum", "room_tbl");
                    loadDropdown(roomNumDropdown1, "roomNum", "room_tbl");
                    fetchTable(roomTbl, "room_tbl");
                    fetchTable(regTbl, "reg_tbl");


                } else {
                    JOptionPane.showMessageDialog(this, "Record failed to save!");
                }
            }
        } catch(JdbcSQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, " Unique index or primary key violation.");
            roomNumTxtFld.setText("");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_addRoomBtnActionPerformed

    private void srchRoomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_srchRoomBtnActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            int roomNum = Integer.parseInt(roomNumDropdown.getSelectedItem().toString());
            pst = con.prepareStatement("SELECT * FROM room_tbl WHERE roomNum=?");
            pst.setInt(1, roomNum);
            rs = pst.executeQuery();
            
            if (rs.next() == true){
                roomNumTxtFld.setText(rs.getString(1));
                roomTierDropdown.setSelectedItem(rs.getObject(2));
                roomPaxTxtFld.setText(rs.getString(3));
                roomPriceTxtFld.setText(rs.getString(4));
            } else {
                JOptionPane.showMessageDialog(this, "No record found!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_srchRoomBtnActionPerformed

    private void edtRoomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtRoomBtnActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            String roomNum = roomNumTxtFld.getText();
            int roomPax = 0;
            double roomPrice = 0;
            String roomTier = roomTierDropdown.getSelectedItem().toString();
            String roomNum2 = roomNumDropdown.getSelectedItem().toString();
            
            switch (roomTier){
                case "Standard" -> {
                    roomPax = 4;
                    roomPrice = 3000.00;
                    break;
                }
                case "Deluxe" -> {
                    roomPax = 6;
                    roomPrice = 4500.00;
                    break;
                }
                case "Executive" -> {
                    roomPax = 10;
                    roomPrice = 8000.00;
                    break;
                }
                case "Presidential" -> {
                    roomPax = 12;
                    roomPrice = 12000.00;
                    break;
                }

            }
            
            pst = con.prepareStatement("UPDATE room_tbl SET roomTier=?, roomPax=?, roomPrice=? WHERE roomNum=?");
            pst.setString(1, roomTier);
            pst.setInt(2, roomPax);
            pst.setDouble(3, roomPrice);
            pst.setString(4, roomNum2);
            
            int k = pst.executeUpdate();
            
            if (k == 1){
                JOptionPane.showMessageDialog(this, "Record updated successfully!");
                roomNumTxtFld.setText("");
                roomPaxTxtFld.setText("");
                roomPriceTxtFld.setText("");
                roomNumTxtFld.requestFocus();
                loadDropdown(roomNumDropdown, "roomNum", "room_tbl");
                loadDropdown(roomNumDropdown1, "roomNum", "room_tbl");
                fetchTable(roomTbl, "room_tbl");
                fetchTable(regTbl, "reg_tbl");

            } else {
                JOptionPane.showMessageDialog(this, "Record failed to delete!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_edtRoomBtnActionPerformed

    private void dltRoomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dltRoomBtnActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            String roomNum = roomNumDropdown.getSelectedItem().toString();
            pst = con.prepareStatement("DELETE FROM room_tbl WHERE roomNum=?");
            pst.setString(1, roomNum);
            
            int k = pst.executeUpdate();
            
            if (k == 1){
                JOptionPane.showMessageDialog(this, "Record deleted successfully!");
                roomNumTxtFld.setText("");
                roomPaxTxtFld.setText("");
                roomPriceTxtFld.setText("");
                roomNumTxtFld.requestFocus();
                loadDropdown(roomNumDropdown, "roomNum", "room_tbl");
                loadDropdown(roomNumDropdown1, "roomNum", "room_tbl");
                fetchTable(roomTbl, "room_tbl");
                fetchTable(regTbl, "reg_tbl");

            }
        }  catch (JdbcSQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(this, "Cannot delete or update a parent row due to constraints");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_dltRoomBtnActionPerformed

    private void srchRegBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_srchRegBtnActionPerformed
        // TODO add your handling code here:
        try {
            fetchTable(regTbl, "reg_tbl");
            // TODO add your handling code here:
            int regNum = Integer.parseInt(regIdDropdown.getSelectedItem().toString());
            pst = con.prepareStatement("SELECT * FROM reg_tbl WHERE regID=?");
            pst.setInt(1, regNum);
            rs = pst.executeQuery();
            
            if (rs.next() == true){
                staySpinner.setValue(rs.getInt(2));
                checkInTxtFld.setText(rs.getString(3));
                checkOutTxtFld.setText(rs.getString(4));
                totalAmountTxtFld.setText(rs.getString(5));
                mopDropdown.setSelectedItem(rs.getString(6));
                lodgerNumDropdown1.setSelectedItem(rs.getInt(7));
                roomNumDropdown1.setSelectedItem(rs.getInt(8));

            } else {
                JOptionPane.showMessageDialog(this, "No record found!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_srchRegBtnActionPerformed

    private void edtRegBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtRegBtnActionPerformed
        try {
            if (!regIDTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "RegID can't be edited!", "", JOptionPane.ERROR_MESSAGE);
                regIDTxtFld.setText("");
            } else if (checkInTxtFld.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Check in date can't be empty!", "", JOptionPane.ERROR_MESSAGE);
            } else {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                double roomPrice = 0;

                int regNum = Integer.parseInt(regIdDropdown.getSelectedItem().toString());
                int roomNum = Integer.parseInt(roomNumDropdown1.getSelectedItem().toString());
                int lodgerNum = Integer.parseInt(lodgerNumDropdown1.getSelectedItem().toString());
                int stay = (int)staySpinner.getValue();
                LocalDate checkInDate = LocalDate.parse(checkInTxtFld.getText(), df);
                if (checkInDate.isBefore(LocalDate.now())){
                    throw new DateIsInThePastException();
                }
                LocalDate checkOutDate = LocalDate.parse(checkOutTxtFld.getText(), df);
                String mop = mopDropdown.getSelectedItem().toString();

                pst = con.prepareStatement("SELECT roomPrice FROM room_tbl WHERE roomNum=?");
                pst.setInt(1, roomNum);

                rs = pst.executeQuery();

                while (rs.next()){
                    roomPrice = rs.getDouble("roomPrice");
                }

                double totalAmount = roomPrice * stay;

                pst = con.prepareStatement("UPDATE reg_tbl SET days=?, checkIn=?, checkOut=?, totalAmount=?, mop=?, lodgerNum=?, roomNum=? WHERE regID=?");
                pst.setInt(1, stay);
                pst.setDate(2, java.sql.Date.valueOf(checkInDate));
                pst.setDate(3, java.sql.Date.valueOf(checkOutDate));
                pst.setDouble(4, totalAmount);
                pst.setString(5, mop);
                pst.setInt(6, lodgerNum);
                pst.setInt(7, roomNum);
                pst.setInt(8, regNum);

                int k = pst.executeUpdate();

                if (k == 1){
                    JOptionPane.showMessageDialog(this, "Record updated successfully!");
                    regIDTxtFld.setText("");
                    checkInTxtFld.setText("");
                    checkOutTxtFld.setText("");
                    totalAmountTxtFld.setText("");
                    staySpinner.setValue(0);
                    regIDTxtFld.requestFocus();
                    loadDropdown(roomNumDropdown1, "roomNum", "room_tbl");
                    loadDropdown(lodgerNumDropdown1, "lodgerNum", "lodger_tbl");
                    loadDropdown(regIdDropdown, "regID", "reg_tbl");
                    fetchTable(regTbl, "reg_tbl");
                } else {
                    JOptionPane.showMessageDialog(this, "Record failed to delete!");
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DateIsInThePastException e){
            JOptionPane.showMessageDialog(this, "Invalid date format. Date is in the past!");
            checkInTxtFld.setText("");
        }
    }//GEN-LAST:event_edtRegBtnActionPerformed

    private void dltRegBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dltRegBtnActionPerformed
        // TODO add your handling code here:
        
        try {
            // TODO add your handling code here:
            int regNum = Integer.parseInt(regIdDropdown.getSelectedItem().toString());
            pst = con.prepareStatement("DELETE FROM reg_tbl WHERE regID=?");
            pst.setInt(1, regNum);
            
            int k = pst.executeUpdate();
            
            if (k == 1){
                JOptionPane.showMessageDialog(this, "Record deleted successfully!");
                regIDTxtFld.setText("");
                checkInTxtFld.setText("");
                staySpinner.setValue(0);
                regIDTxtFld.requestFocus();
                loadDropdown(regIdDropdown, "regID", "reg_tbl");
                loadDropdown(roomNumDropdown1, "roomNum", "room_tbl");
                loadDropdown(lodgerNumDropdown1, "lodgerNum", "lodger_tbl");
                fetchTable(regTbl, "reg_tbl");

            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_dltRegBtnActionPerformed

    private void lodgerNumTxtFldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lodgerNumTxtFldKeyPressed
        char c = evt.getKeyChar();
        if (Character.isDigit(c) || Character.isISOControl(c)){
            lodgerNumTxtFld.setEditable(true);
        } else {
            lodgerNumTxtFld.setEditable(false);
        }
    }//GEN-LAST:event_lodgerNumTxtFldKeyPressed

    private void fnameTxtFldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fnameTxtFldKeyPressed
        char c = evt.getKeyChar();
        if (Character.isLetter(c) || Character.isISOControl(c) || Character.isWhitespace(c)){
            fnameTxtFld.setEditable(true);
        } else {
            fnameTxtFld.setEditable(false);
        }
    }//GEN-LAST:event_fnameTxtFldKeyPressed

    private void lnameTxtFldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lnameTxtFldKeyPressed
        char c = evt.getKeyChar();
        if (Character.isLetter(c) || Character.isISOControl(c) || Character.isWhitespace(c)){
            lnameTxtFld.setEditable(true);
        } else {
            lnameTxtFld.setEditable(false);
        }
    }//GEN-LAST:event_lnameTxtFldKeyPressed

    private void cityTxtFldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cityTxtFldKeyPressed
        char c = evt.getKeyChar();
        if (Character.isLetter(c) || Character.isISOControl(c) || Character.isWhitespace(c)){
            cityTxtFld.setEditable(true);
        } else {
            cityTxtFld.setEditable(false);
        }
    }//GEN-LAST:event_cityTxtFldKeyPressed

    private void contactNumTxtFldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contactNumTxtFldKeyPressed
        char c = evt.getKeyChar();

        if (Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
            contactNumTxtFld.setEditable(true);

        } else {
            contactNumTxtFld.setEditable(false);
        }
    }//GEN-LAST:event_contactNumTxtFldKeyPressed

    private void regIDTxtFldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_regIDTxtFldKeyPressed
        char c = evt.getKeyChar();
        if (Character.isDigit(c) || Character.isISOControl(c)){
            regIDTxtFld.setEditable(true);
        } else {
            regIDTxtFld.setEditable(false);
        }
    }//GEN-LAST:event_regIDTxtFldKeyPressed

    private void roomNumTxtFldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roomNumTxtFldKeyPressed
        char c = evt.getKeyChar();
        if (Character.isDigit(c) || Character.isISOControl(c)){
            roomNumTxtFld.setEditable(true);
        } else {
            roomNumTxtFld.setEditable(false);
        }
    }//GEN-LAST:event_roomNumTxtFldKeyPressed

    private void roomNumDropdown1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomNumDropdown1ActionPerformed
        try {
            loadDropdown(roomNumDropdown1, "roomNum", "room_tbl");
            int roomNum = 0;
            if (roomNumDropdown1.getSelectedItem() == null){
                System.out.println("Room table has no items yet.");
            } else {
                roomNum = Integer.parseInt(roomNumDropdown1.getSelectedItem().toString());
            }
            double roomPrice = 0;
            pst = con.prepareStatement("SELECT roomPrice FROM room_tbl WHERE roomNum = ?");
            pst.setInt(1, roomNum);
            rs = pst.executeQuery();
            
            while (rs.next()){
                roomPrice = rs.getDouble("roomPrice");
            }
            
            int stay = (int)staySpinner.getValue();
            
            double totalAmount = roomPrice * stay;
            
            totalAmountTxtFld.setText(String.valueOf(totalAmount));
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }//GEN-LAST:event_roomNumDropdown1ActionPerformed

    private void checkInTxtFldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkInTxtFldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkInTxtFldActionPerformed

    private void checkInTxtFldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkInTxtFldKeyPressed
        try {
            char c = evt.getKeyChar();
            if (checkInTxtFld.getText().length() < 10){
                // date is not complete
            } else {
                String date = checkInTxtFld.getText();
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate checkInDate = LocalDate.parse(date, df);
                if (checkInDate.isBefore(LocalDate.now())){
                    throw new DateIsInThePastException();
                }
                int stay = (int)staySpinner.getValue();
                LocalDate checkOutDate = checkInDate.plusDays(stay);
                checkOutTxtFld.setText(checkOutDate.toString());
            }
        } catch (DateTimeParseException e){
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
            checkInTxtFld.setText("");
        } catch (NullPointerException e){
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
            checkInTxtFld.setText("");
        } catch (DateIsInThePastException e){
            JOptionPane.showMessageDialog(this, "Invalid date format. Date is in the past!");
            checkInTxtFld.setText("");
        }
    }//GEN-LAST:event_checkInTxtFldKeyPressed

    private void roomTierDropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomTierDropdownActionPerformed
        String roomTier = roomTierDropdown.getSelectedItem().toString();
        int roomPax = 0;
        double roomPrice = 0;
        switch (roomTier){
            case "Standard" -> {
                roomPax = 4;
                roomPrice = 3000.00;
                break;
            }
            case "Deluxe" -> {
                roomPax = 6;
                roomPrice = 4500.00;
                break;
            }
            case "Executive" -> {
                roomPax = 10;
                roomPrice = 8000.00;
                break;
            }
            case "Presidential" -> {
                roomPax = 12;
                roomPrice = 12000.00;
                break;
            }
        }
        
        roomPaxTxtFld.setText(String.valueOf(roomPax));
        roomPriceTxtFld.setText(String.valueOf(roomPrice));

    }//GEN-LAST:event_roomTierDropdownActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Main().setVisible(true);
                    
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLodgerBtn;
    private javax.swing.JButton addRegBtn;
    private javax.swing.JButton addRoomBtn;
    private javax.swing.JTextField checkInTxtFld;
    private javax.swing.JTextField checkOutTxtFld;
    private javax.swing.JTextField cityTxtFld;
    private javax.swing.JTextField contactNumTxtFld;
    private javax.swing.JButton dltLodgerBtn;
    private javax.swing.JButton dltRegBtn;
    private javax.swing.JButton dltRoomBtn;
    private javax.swing.JButton editLodgerBtn;
    private javax.swing.JButton edtRegBtn;
    private javax.swing.JButton edtRoomBtn;
    private javax.swing.JTextField emailAddressTxtFld;
    private javax.swing.JTextField fnameTxtFld;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField lnameTxtFld;
    private javax.swing.JComboBox<String> lodgerNumDropdown;
    private javax.swing.JComboBox<String> lodgerNumDropdown1;
    private javax.swing.JTextField lodgerNumTxtFld;
    private javax.swing.JPanel lodgerPanel;
    private javax.swing.JTable lodgerTbl;
    private javax.swing.JComboBox<String> mopDropdown;
    private javax.swing.JPanel navbar;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JTextField regIDTxtFld;
    private javax.swing.JComboBox<String> regIdDropdown;
    private javax.swing.JTable regTbl;
    private javax.swing.JPanel registrationPanel;
    private javax.swing.JComboBox<String> roomNumDropdown;
    private javax.swing.JComboBox<String> roomNumDropdown1;
    private javax.swing.JTextField roomNumTxtFld;
    private javax.swing.JPanel roomPanel;
    private javax.swing.JTextField roomPaxTxtFld;
    private javax.swing.JTextField roomPriceTxtFld;
    private javax.swing.JTable roomTbl;
    private javax.swing.JComboBox<String> roomTierDropdown;
    private javax.swing.JButton searchLodgerBtn;
    private javax.swing.JButton srchRegBtn;
    private javax.swing.JButton srchRoomBtn;
    private javax.swing.JSpinner staySpinner;
    private javax.swing.JTextField streetTxtFld;
    private javax.swing.JPanel tblPnl;
    private javax.swing.JPanel tblPnl1;
    private javax.swing.JPanel tblPnl2;
    private javax.swing.JLabel title;
    private javax.swing.JLabel title1;
    private javax.swing.JLabel title2;
    private javax.swing.JPanel titleNavPanel;
    private javax.swing.JPanel titleNavPanel1;
    private javax.swing.JPanel titleNavPanel2;
    private javax.swing.JButton toLodgerBtn;
    private javax.swing.JButton toRegistrationBtn;
    private javax.swing.JButton toRoomBtn;
    private javax.swing.JTextField totalAmountTxtFld;
    private javax.swing.JPanel txtFldPnl;
    private javax.swing.JPanel txtFldPnl1;
    private javax.swing.JPanel txtFldPnl2;
    // End of variables declaration//GEN-END:variables

    private static class DateIsInThePastException extends Exception {

        public DateIsInThePastException() {
        }
    }
}
