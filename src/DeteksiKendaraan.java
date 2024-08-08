//package deteksikendaraan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

public class DeteksiKendaraan extends JFrame {

    private JTextArea textArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DeteksiKendaraan().setVisible(true);
            }
        });
    }

    public DeteksiKendaraan() {
        setTitle("Deteksi Kendaraan");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            runMainFunction();
                        } catch (Exception ex) {
                            Logger.getLogger(DeteksiKendaraan.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(okButton, BorderLayout.SOUTH);
        add(panel);

        // Redirect System.out to textArea
        PrintStream printStream = new PrintStream(new TextAreaOutputStream(textArea));
        System.setOut(printStream);
        System.setErr(printStream);
    }

    class TextAreaOutputStream extends OutputStream {
        private final JTextArea textArea;

        public TextAreaOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }

        @Override
        public void write(byte[] b, int off, int len) {
            textArea.append(new String(b, off, len));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }

    public void runMainFunction() throws Exception {
        try {
            System.out.println("Starting application...");
            String db = "trajectorybdg";
            System.out.println("Connected to database : " + db);

            int totalrow = num();
            // System.out.println(totalrow);
            countHeadingAngle(totalrow);
            // countSpatialDistance(totalrow);
            // countTemporalInterval(totalrow);
            // countVelocity(totalrow);
            // countAcceleration(totalrow);
            // countHeadingChange(totalrow);
            // countMeanVelocity(totalrow);
            // countExpectationVelocity(totalrow);
            // System.out.println(UpperBoundVelocity());
            // System.out.println(UpperBoundAcceleration());
            // pointsType(totalrow);
            // Segmentation(totalrow);
            // Segmentation2();
            // SegmentationNumbering(totalrow);
            // SubSegmentID();
            // SegmentStatus();
            // StartEndSegment();
            // countSegmentDist();
            // countMaxVi();
            // countMaxAi();
            // countAV();
            // countEV();
            // countDV();
            // countHCR();
            // countSR();
            // countVCR();
            System.out.println("Application finished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int num() throws Exception
    {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/trajectorybdg?";
        String username = "root";
        String password = "";

        Class.forName(driver);
        Connection connect = DriverManager.getConnection(url + "user=" + username + "&password=" + password);

        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from gpsp");
        int count = 0;
        while (resultSet.next())
        {
            count++;
        }
        return count;
    }

    public static void updateRow(int rownumber, String colname, double data) throws ClassNotFoundException, SQLException
    {
        Connection connect = null;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

            Statement statement = connect.createStatement();

            String sql = "UPDATE gpsp SET " + colname + "=" + data  + "WHERE no = " + rownumber + "";
            statement.executeUpdate(sql);
        }
        catch(SQLException sqlEx)
        {
            /*To catch any SQLException thrown during DB
             *Operations and continue processing like sending alert to admin
             *that exception occurred.
             */
        }
        finally
        {
            if(connect!=null)
                connect.close();
        }
    }

    public static void updateRowString(int rownumber, String colname, String data) throws ClassNotFoundException, SQLException
    {
        Connection connect = null;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

            Statement statement = connect.createStatement();

            String sql = "UPDATE gpsp SET " + colname + "='" + data  + "' WHERE no = " + rownumber + "";
            statement.executeUpdate(sql);
        }
        catch(SQLException sqlEx)
        {
            /*To catch any SQLException thrown during DB
             *Operations and continue processing like sending alert to admin
             *that exception occurred.
             */
        }
        finally
        {
            if(connect!=null)
                connect.close();
        }
    }

    public static void countHeadingAngle(int totalrow) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        double headingangle;
        double ptql = 0;
        double ptqlo = 0;
        String type = "";
        String rowname = "headingangle";

        for(int i=1; i <= totalrow; i++)
        {
            ResultSet ch = statement.executeQuery("select trip from gpsp where no=" + i);
            ch.next();
            int chTrip = ch.getInt(1);
            int chTrip1;
            if( i < totalrow)
            {
                ResultSet ch1 = statement.executeQuery("select trip from gpsp where no=" + (i+1));
                ch1.next();
                chTrip1 = ch1.getInt(1);
            }
            else
            {
                chTrip1 = chTrip;
            }
            if(chTrip == chTrip1 && i < totalrow)
            {
                ResultSet rs = statement.executeQuery("select latitude from gpsp where no=" + i);
                rs.next();
                double latitude = rs.getDouble(1);
                ResultSet rs1 = statement.executeQuery("select longitude from gpsp where no=" + i);
                rs1.next();
                double longitude = rs1.getDouble(1);
                ResultSet rs2 = statement.executeQuery("select latitude from gpsp where no=" + (i+1));
                rs2.next();
                double latitude1 = rs2.getDouble(1);
                ResultSet rs3 = statement.executeQuery("select longitude from gpsp where no=" + (i+1));
                rs3.next();
                double longitude1 = rs3.getDouble(1);
                ResultSet rs4 = statement.executeQuery("select type from gpsp where no=" + (i+1));
                rs4.next();
                type = rs4.getString(1);

                Point q = new Point(latitude, longitude);
                Point r = new Point(latitude1, longitude1);

                ptql = q.getl();
                ptqlo = q.getlo();
                headingangle = q.getAngle(r);
            }
            else
            {
                headingangle = 0;
            }
            updateRow(i, rowname, headingangle);
            System.out.print(i + " ");
            System.out.print(chTrip + " ");
            System.out.print(chTrip1 + " ");
            System.out.print(ptql + " ");
            System.out.print(ptqlo + " ");
            System.out.print(chTrip1 + " ");
            System.out.print(chTrip1 + " ");
            System.out.print(headingangle + " ");
            System.out.println(type);
        }
    }

    public static void countSpatialDistance(int totalrow) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        double spatialdistance;
        double ptql;
        ptql = 0.0;
        double ptqlo;
        //double ptrl;
        //double ptrlo;
        ptqlo = 0.0;

        String type = "";
        String rowname = "spatialdistance";

        for(int i=1; i <= totalrow; i++)
        {
            ResultSet ch = statement.executeQuery("select trip from gpsp where no=" + i);
            ch.next();
            int chTrip = ch.getInt(1);
            int chTrip1;
            if( i < totalrow)
            {
                ResultSet ch1 = statement.executeQuery("select trip from gpsp where no=" + (i+1));
                ch1.next();
                chTrip1 = ch1.getInt(1);
            }
            else
            {
                chTrip1 = chTrip;
            }
            if(chTrip == chTrip1 && i < totalrow)
            {
                ResultSet rs = statement.executeQuery("select latitude from gpsp where no=" + i);
                rs.next();
                double latitude = rs.getDouble(1);
                ResultSet rs1 = statement.executeQuery("select longitude from gpsp where no=" + i);
                rs1.next();
                double longitude = rs1.getDouble(1);
                ResultSet rs2 = statement.executeQuery("select latitude from gpsp where no=" + (i+1));
                rs2.next();
                double latitude1 = rs2.getDouble(1);
                ResultSet rs3 = statement.executeQuery("select longitude from gpsp where no=" + (i+1));
                rs3.next();
                double longitude1 = rs3.getDouble(1);
                ResultSet rs4 = statement.executeQuery("select type from gpsp where no=" + (i+1));
                rs4.next();
                type = rs4.getString(1);

                Point q;
                q = new Point(latitude, longitude);
                Point r;
                r = new Point(latitude1, longitude1);

                ptql = q.getl();
                ptqlo = q.getlo();

                r.getl();
                r.getlo();

                spatialdistance = q.haversine(r);
            }
            else
            {
                spatialdistance = 0;
            }
            updateRow(i, rowname, spatialdistance);
            System.out.print(i + " ");
            System.out.print(chTrip + " ");
            System.out.print(chTrip1 + " ");
            System.out.print(ptql + " ");
            System.out.print(ptqlo + " ");
            System.out.print(chTrip1 + " ");
            System.out.print(chTrip1 + " ");
            System.out.print(spatialdistance + " ");
            System.out.println(type);
        }
    }

    public static void countTemporalInterval(int totalrow) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        double temporalinterval;
        String datestart;
        String datestop;
        String rowname = "temporalinterval";

        for(int i=1; i <= totalrow; i++)
        {
            ResultSet ch = statement.executeQuery("select trip from gpsp where no=" + i);
            ch.next();
            int chTrip = ch.getInt(1);
            int chTrip1;
            if( i < totalrow)
            {
                ResultSet ch1 = statement.executeQuery("select trip from gpsp where no=" + (i+1));
                ch1.next();
                chTrip1 = ch1.getInt(1);
            }
            else
            {
                chTrip1 = chTrip;
            }
            if(chTrip == chTrip1 && i < totalrow)
            {

                ResultSet rs = statement.executeQuery("select timestamp from gpsp where no=" + i);
                rs.next();
                datestart = rs.getString(1);
                ResultSet rs1 = statement.executeQuery("select timestamp from gpsp where no=" + (i+1));
                rs1.next();
                datestop = rs1.getString(1);

                //datestart = "2017-08-01 19:38:52";
                //datestop = "2017-08-01 21:34:30";

                SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

                Date d1 = null;
                Date d2 = null;
                try {
                    d1 = format.parse(datestart);
                    d2 = format.parse(datestop);
                }
                catch (ParseException e)
                {

                }
                finally
                {
                    double diff;
                    diff = d2.getTime() - d1.getTime();
                    //float diffSeconds = (diff / 1000);
                    //float diffMinutes = diff / (60 * 1000);
                    double diffHours;
                    diffHours = diff / (60 * 60 * 1000);
                    //System.out.println(datestart);
                    //System.out.println(datestop);
                    //System.out.println("Time in seconds: " + diffSeconds + " seconds.");
                    //System.out.println("Time in minutes: " + diffMinutes + " minutes.");


                    //System.out.println("Time in hours: " + diffHours + " hours.");
                    //in hours
                    temporalinterval = diffHours;
                }
            }
            else
            {
                temporalinterval = 0;
            }
           updateRow(i, rowname, temporalinterval);
            System.out.print(i + " ");
            System.out.println(temporalinterval + " ");
        }
    }

    public static void countVelocity(int totalrow) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        double velocity;
        double spatialdistance;
        double temporalinterval;
        String rowname = "velocity";

        for(int i=1; i <= totalrow; i++)
        {
            ResultSet ch = statement.executeQuery("select trip from gpsp where no=" + i);
            ch.next();
            int chTrip = ch.getInt(1);
            int chTrip1;
            if( i < totalrow)
            {
                ResultSet ch1 = statement.executeQuery("select trip from gpsp where no=" + (i+1));
                ch1.next();
                chTrip1 = ch1.getInt(1);
            }
            else
            {
                chTrip1 = chTrip;
            }
            if(chTrip == chTrip1 && i < totalrow)
            {

                ResultSet rs = statement.executeQuery("select spatialdistance from gpsp where no=" + i);
                rs.next();
                spatialdistance = rs.getDouble(1);
                ResultSet rs1 = statement.executeQuery("select temporalinterval from gpsp where no=" + (i+1));
                rs1.next();
                temporalinterval = rs1.getDouble(1);
                if(temporalinterval == 0.0)
                {
                    temporalinterval = 0.01;
                }
                velocity = spatialdistance / temporalinterval;
            }
            else
            {
                velocity = 0;
            }
            updateRow(i, rowname, velocity);
            System.out.print(i + " ");
            System.out.println(velocity+ " ");
        }
    }

    public static void countAcceleration(int totalrow) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        double acceleration;
        double velocity;
        double velocity1;
        double temporalinterval;
        String rowname = "acceleration";

        for(int i=1; i <= totalrow; i++)
        {
            ResultSet ch = statement.executeQuery("select trip from gpsp where no=" + i);
            ch.next();
            int chTrip = ch.getInt(1);
            int chTrip1;
            if( i < totalrow)
            {
                ResultSet ch1 = statement.executeQuery("select trip from gpsp where no=" + (i+1));
                ch1.next();
                chTrip1 = ch1.getInt(1);
            }
            else
            {
                chTrip1 = chTrip;
            }
            if(chTrip == chTrip1 && i < totalrow)
            {

                ResultSet rs = statement.executeQuery("select velocity from gpsp where no=" + i);
                rs.next();
                velocity = rs.getDouble(1);
                 ResultSet rs1 = statement.executeQuery("select velocity from gpsp where no=" + (i+1));
                rs1.next();
                velocity1 = rs1.getDouble(1);
                ResultSet rs2 = statement.executeQuery("select temporalinterval from gpsp where no=" + (i));
                rs2.next();
                temporalinterval = rs2.getDouble(1);
                if(temporalinterval == 0.0)
                {
                    temporalinterval = 0.01;
                }
                acceleration = (velocity1 - velocity) / temporalinterval;
            }
            else
            {
                acceleration = 0;
            }
            updateRow(i, rowname, acceleration);
            System.out.print(i + " ");
            System.out.println(acceleration + " ");
        }
    }

    public static void countMeanVelocity(int totalrow) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int point;
        double meanvelocity;
        double sumspatialdistance;
        double temporalinterval1;
        double temporalinterval2;
        double aggtemporalinterval;
        String rowname = "meanvelocity";

        for(int i=54844; i <= totalrow; i++)
        {
            ResultSet ch = statement.executeQuery("select trip from gpsp where no=" + i);
            ch.next();
            int chTrip = ch.getInt(1);
            int chTrip1;
            if( i < totalrow)
            {
                ResultSet ch1 = statement.executeQuery("select trip from gpsp where no=" + (i+1));
                ch1.next();
                chTrip1 = ch1.getInt(1);
            }
            else
            {
                chTrip1 = chTrip;
            }
            if(chTrip == chTrip1 && i < totalrow)
            {
                ResultSet ro = statement.executeQuery("select point from gpsp where no=" + i);
                ro.next();
                point = ro.getInt(1);
                ResultSet rs = statement.executeQuery("select sum(spatialdistance) from gpsp where trip =" + chTrip + " and point between " + 1 + " and " + point);
                rs.next();
                sumspatialdistance = rs.getDouble(1);
                ResultSet rs2 = statement.executeQuery("select temporalinterval from gpsp where trip =" + chTrip + " and point=" + 1);
                rs2.next();
                temporalinterval1 = rs2.getDouble(1);
                if(temporalinterval1 == 0.0)
                {
                    temporalinterval1 = 1;
                }
                ResultSet rs3 = statement.executeQuery("select temporalinterval from gpsp where trip =" + chTrip + " and point =" + point);
                rs3.next();
                temporalinterval2 = rs3.getDouble(1);
                if(temporalinterval2 == 0.0)
                {
                    temporalinterval2 = 1;
                }
                aggtemporalinterval = temporalinterval2 - temporalinterval1;
                if(aggtemporalinterval == 0.0)
                {
                    aggtemporalinterval = 1;
                }
                meanvelocity = sumspatialdistance / aggtemporalinterval;
            }
            else
            {
                meanvelocity = 0;
            }
            updateRow(i, rowname, meanvelocity);
            System.out.print(i + " ");
            System.out.println(meanvelocity + " ");
        }
    }

    public static void countExpectationVelocity(int totalrow) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int point;
        double expectationvelocity;
        double sumvelocity;
        int countvelocity;
        String rowname = "expectationvelocity";

        for(int i=1; i <= totalrow; i++)
        {
            ResultSet ch = statement.executeQuery("select trip from gpsp where no=" + i);
            ch.next();
            int chTrip = ch.getInt(1);
            int chTrip1;
            if( i < totalrow)
            {
                ResultSet ch1 = statement.executeQuery("select trip from gpsp where no=" + (i+1));
                ch1.next();
                chTrip1 = ch1.getInt(1);
            }
            else
            {
                chTrip1 = chTrip;
            }
            if(chTrip == chTrip1 && i < totalrow)
            {
                ResultSet ro = statement.executeQuery("select point from gpsp where no=" + i);
                ro.next();
                point = ro.getInt(1);
                ResultSet rs = statement.executeQuery("select sum(velocity) from gpsp where trip =" + chTrip + " and point between " + 1 + " and " + point);
                rs.next();
                sumvelocity = rs.getDouble(1);
                ResultSet rs2 = statement.executeQuery("select count(velocity) from gpsp where trip =" + chTrip + " and point between " + 1 + " and " + point);
                rs2.next();
                countvelocity = rs2.getInt(1);

                expectationvelocity = sumvelocity / countvelocity;
            }
            else
            {
                expectationvelocity = 0;
            }
            updateRow(i, rowname, expectationvelocity);
            System.out.print(i + " ");
            System.out.println(expectationvelocity + " ");
        }
    }

    public static void countHeadingChange(int totalrow) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        double headingchange = 0;
        String rowname = "headingchange";

        for(int i=1; i <= totalrow; i++)
        {
            ResultSet ch0 = statement.executeQuery("select point from gpsp where no=" + i);
            ch0.next();
            int point = ch0.getInt(1);
            if(point == 1)
            {
                headingchange = 0;
            }
            else
            {
                ResultSet ch = statement.executeQuery("select trip from gpsp where no=" + (i-1));
                ch.next();
                int chTrip = ch.getInt(1);
                int chTrip1;
                if( i < totalrow)
                {
                    ResultSet ch1 = statement.executeQuery("select trip from gpsp where no=" + i);
                    ch1.next();
                    chTrip1 = ch1.getInt(1);
                }
                else
                {
                    chTrip1 = chTrip;
                }
                if(chTrip == chTrip1)
                {
                    ResultSet rs = statement.executeQuery("select headingangle from gpsp where no=" + (i-1));
                    rs.next();
                    double headingangle1 = rs.getDouble(1);
                    ResultSet rs1 = statement.executeQuery("select headingangle from gpsp where no=" + i);
                    rs1.next();
                    double headingangle2 = rs1.getDouble(1);

                    headingchange = headingangle2 - headingangle1;
                }
            }
            updateRow(i, rowname, headingchange);
            System.out.print(i + " ");
            System.out.println(headingchange + " ");
        }
    }

    public static double UpperBoundVelocity(String type) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        double ubvelocity;

        ResultSet ch = statement.executeQuery("SELECT MAX(velocity) FROM trajectorybdg.gpsp where type ='" + type + "'");
        ch.next();
        ubvelocity = ch.getDouble(1);

        return ubvelocity;
    }

    public static double UpperBoundVelocity() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        double ubvelocity;

        ResultSet ch = statement.executeQuery("SELECT MAX(velocity) FROM trajectorybdg.gpsp");
        ch.next();
        ubvelocity = ch.getDouble(1);

        return ubvelocity;
    }

    public static double UpperBoundAcceleration(String type) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        double ubacceleration;

        ResultSet ch = statement.executeQuery("SELECT MAX(acceleration) FROM trajectorybdg.gpsp where type ='" + type + "'");
        ch.next();
        ubacceleration = ch.getDouble(1);

        return ubacceleration;
    }

    public static double UpperBoundAcceleration() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        double ubacceleration;

        ResultSet ch = statement.executeQuery("SELECT MAX(acceleration) FROM trajectorybdg.gpsp");
        ch.next();
        ubacceleration = ch.getDouble(1);

        return ubacceleration;
    }

    public static void pointsType(int totalrow) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        String pointstype;
        String rowname = "pointstype";

        String type = "walk";

        double ubvelocity = UpperBoundVelocity(type);
        double ubacceleration = UpperBoundAcceleration(type);

        for(int i=1; i <= totalrow; i++)
        {
            ResultSet ch0 = statement.executeQuery("select velocity from gpsp where no=" + i);
            ch0.next();
            int velocity = ch0.getInt(1);
            ResultSet ch1 = statement.executeQuery("select acceleration from gpsp where no=" + i);
            ch1.next();
            int acceleration = ch1.getInt(1);

            if(velocity < ubvelocity && acceleration < ubacceleration)
            {
               pointstype = "Walk Point";
            }
            else if(velocity > ubvelocity || acceleration > ubacceleration)
            {
               pointstype = "Non-Walk Point";
            }
            else
            {
               pointstype = "?";
            }
            updateRowString(i, rowname, pointstype);
            System.out.print(i + " ");
            System.out.println(pointstype);
        }
    }

    public static void Segmentation(int totalrow) throws ClassNotFoundException, SQLException
    {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        String type;
        String rowname = "segment";
        String segment;

        //step 1
        for(int i=1; i <= totalrow; i++)
        {
            ResultSet ch1 = statement.executeQuery("select type from gpsp where no=" + i);
            ch1.next();
            type = ch1.getString(1);
            if(type.equals("walk"))
            {
                segment = "walk segment";
            }
            else
            {
                segment = "non-walk segment";
            }

            updateRowString(i, rowname, segment);
            System.out.print(i + " ");
            System.out.println(segment);
        }
    }

    public static void Segmentation2() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaltrip;
        int totaluser;
        double sumspatialdistance;
        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        //step 2
        ResultSet ch = statement.executeQuery("select count(distinct userid) from gpsp");
        ch.next();
        totaluser = ch.getInt(1);

        for(int h=1; h <= totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
                ResultSet ch2 = statement.executeQuery("select sum(spatialdistance) from gpsp where pointstype= 'Walk Point' and trip=" + i +" and userid=" + h);
                ch2.next();
                sumspatialdistance = ch2.getDouble(1);
                if(sumspatialdistance < 0.05)
                {
                    String sql = "UPDATE gpsp SET segment='non-walk segment' WHERE pointstype = 'Walk Point' and trip =" + i +" and userid=" + h;
                    statement.executeUpdate(sql);
                    System.out.println("Non-Walk segment of userid " + h + " in trip " + i + " updated");
                }

                ResultSet ch3 = statement.executeQuery("select sum(spatialdistance) from gpsp where pointstype= 'Non-Walk Point' and trip=" + i +" and userid=" + h);
                ch3.next();
                sumspatialdistance = ch3.getDouble(1);
                if(sumspatialdistance < 0.05)
                {
                    String sql = "UPDATE gpsp SET segment='walk segment' WHERE pointstype = 'Non-Walk Point' and trip =" + i +" and userid=" + h;
                    statement.executeUpdate(sql);
                    System.out.println("Walk segment of userid " + h + " in trip " + i + " updated");
                }
            }
        }
        System.out.println("Update Finished");
    }

    public static void SegmentationNumbering(int totalrow) throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        String segment1;
        String segment2;
        String rowname = "segmentnumber";
        int segmentnumber = 1;
        int point;

        for(int i=1; i <= totalrow; i++)
        {
            ResultSet rs = statement.executeQuery("select point from gpsp where no=" + i);
            rs.next();
            point = rs.getInt(1);
            if(point == 1)
            {
                segmentnumber = 1;
            }
            else
            {
                ResultSet ch = statement.executeQuery("select trip from gpsp where no=" + (i-1));
                ch.next();
                int chTrip = ch.getInt(1);
                int chTrip1;
                if( i < totalrow)
                {
                    ResultSet ch1 = statement.executeQuery("select trip from gpsp where no=" + (i));
                    ch1.next();
                    chTrip1 = ch1.getInt(1);
                }
                else
                {
                    chTrip1 = chTrip;
                }
                if(chTrip == chTrip1 && i < totalrow)
                {

                    ResultSet rs0 = statement.executeQuery("select segment from gpsp where no=" + (i-1));
                    rs0.next();
                    segment1 = rs0.getString(1);
                    ResultSet rs1 = statement.executeQuery("select segment from gpsp where no=" + (i));
                    rs1.next();
                    segment2 = rs1.getString(1);
                    if(segment1.equals(segment2))
                    {
                       //do nothin
                    }
                    else
                    {
                        segmentnumber++;
                    }
                }
                else
                {
                    segmentnumber = 1;
                }
            }
            updateRow(i, rowname, segmentnumber);
            System.out.print(i + " ");
            System.out.println(segmentnumber + " ");
        }
    }

    public static void SubSegmentID() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaluser;
        int totaltrip;
        int totalsegment;
        int rownumber;
        int countrow;
        String rowname = "subsegmentid";

        ResultSet ch0 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch0.next();
        totaluser = ch0.getInt(1);

        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        for(int h=1; h <= totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
                ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where userid =" + h +" and trip =" + i);
                ch.next();
                totalsegment = ch.getInt(1);
                for(int j=1; j <= totalsegment; j++)
                {
                    String sql = "select no from gpsp WHERE userid=" + h + " and trip=" + i + " and segmentnumber=" + j + " order by timestamp asc";
                    ResultSet rs = statement.executeQuery(sql);
                    countrow = 1;

                    while(rs.next())
                    {
                        rownumber = rs.getInt(1);
                        updateRow(rownumber, rowname, countrow);
                        System.out.println("SubSegmentID of no " + rownumber + " from segment " + j + " is " + countrow);
                        countrow++;
                    }
                }
            }
        }
    }

    public static void SegmentStatus() throws ClassNotFoundException, SQLException
    {
        //Step 3
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaluser;
        int totaltrip;
        int totalsegment;
        double sumspatialdistance;
        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        ResultSet ch3 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch3.next();
        totaluser = ch3.getInt(1);

        for(int h=1; h < totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
               ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where trip =" + i +" and userid =" +h);
               ch.next();
               totalsegment = ch.getInt(1);
               for(int j=1; j <= totalsegment; j++)
               {
                   ResultSet ch2 = statement.executeQuery("select sum(spatialdistance) from gpsp where trip=" + i +" and segmentnumber=" + j +" and userid =" +h);
                   ch2.next();
                   sumspatialdistance = ch2.getDouble(1);
                   if(sumspatialdistance < 0.05)
                   {
                       String sql = "UPDATE gpsp SET segmentstatus='uncertain segment' WHERE trip =" + i + " and segmentnumber=" + j +" and userid =" +h;
                       statement.executeUpdate(sql);
                       System.out.println("Segment " + j + " of userid " + h + " in trip " + i + " is uncertain");
                   }
                   else
                   {
                       String sql = "UPDATE gpsp SET segmentstatus='certain segment' WHERE trip =" + i + " and segmentnumber=" + j +" and userid =" +h;
                       statement.executeUpdate(sql);
                       System.out.println("Segment " + j + " of userid " + h + " in trip " + i + " is certain");
                   }
                }
            }
        }

    }

    public static void StartEndSegment() throws ClassNotFoundException, SQLException
    {
        //Step 4
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaltrip;
        int totalsegment;
        int totaluser;
        int point;

        ResultSet ch0 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch0.next();
        totaluser = ch0.getInt(1);

        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        for(int h=1; h <= totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
                ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where userid = " + h + " and trip =" + i);
                ch.next();
                totalsegment = ch.getInt(1);
                for(int j=1; j <= totalsegment; j++)
                {
                    String sql = "UPDATE gpsp SET startend='start' WHERE userid = "+ h +" and trip ="+ i +" and segmentnumber =" + j + " order by timestamp asc limit 1";
                    statement.executeUpdate(sql);
                    ResultSet cres = statement.executeQuery("select point from gpsp where userid = " + h + " and trip =" + i + " and segmentnumber =" + j + " order by timestamp asc limit 1");
                    cres.next();
                    point = cres.getInt(1);
                    System.out.println("Point " + point + " from userid " + h + " in trip " + i + " is start point in Segment " + j);

                    String sql2 = "UPDATE gpsp SET startend='end' WHERE userid =" + h + " and trip =" + i + " and segmentnumber =" + j + " order by timestamp desc limit 1";
                    statement.executeUpdate(sql2);
                    ResultSet cres2 = statement.executeQuery("select point from gpsp where userid =" + h + " and trip =" + i + " and segmentnumber =" + j + " order by timestamp desc limit 1");
                    cres2.next();
                    point = cres2.getInt(1);
                    System.out.println("Point " + point + " from userid " + h + " in trip " + i + " is end point in Segment " + j);
                }
            }
        }
    }

    public static void countSegmentDist() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaltrip;
        int totalsegment;
        int totaluser;
        double segmentDist;

        ResultSet ch0 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch0.next();
        totaluser = ch0.getInt(1);

        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        for(int h=1; h <= totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
                ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where trip =" + i +" and userid =" + h);
                ch.next();
                totalsegment = ch.getInt(1);

                for(int j=1; j <= totalsegment; j++)
                {
                    ResultSet ch5 = statement.executeQuery("select sum(spatialdistance) from gpsp where userid =" + h + " and trip =" + i +" and segmentnumber=" + j);
                    ch5.next();
                    segmentDist = ch5.getDouble(1);

                    String sql = "UPDATE gpsp SET segmentDist=" + segmentDist + " where trip =" + i +" and userid =" + h + " and segmentnumber=" + j;
                    statement.executeUpdate(sql);
                    System.out.println("Segment distance of Segment " + j + " of trip " + i + " of userid " + h + " is " + segmentDist);
                }
            }
        }
    }

    public static void countMaxVi() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaltrip;
        int totalsegment;
        int totaluser;
        double maxvi;

        ResultSet ch0 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch0.next();
        totaluser = ch0.getInt(1);

        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        for(int h=1; h <= totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
                ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where trip =" + i +" and userid =" + h);
                ch.next();
                totalsegment = ch.getInt(1);

                for(int j=1; j <= totalsegment; j++)
                {
                    ResultSet ch5 = statement.executeQuery("select max(velocity) from gpsp where userid =" + h + " and trip =" + i +" and segmentnumber=" + j);
                    ch5.next();
                    maxvi = ch5.getDouble(1);

                    String sql = "UPDATE gpsp SET segmentmaxvi=" + maxvi + " where trip =" + i +" and userid =" + h + " and segmentnumber=" + j;
                    statement.executeUpdate(sql);
                    System.out.println("Maximum Velocity of Segment " + j + " of trip " + i + " of userid " + h + " is " + maxvi);
                }
            }
        }
    }

    public static void countMaxAi() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaltrip;
        int totalsegment;
        int totaluser;
        double maxai;

        ResultSet ch0 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch0.next();
        totaluser = ch0.getInt(1);

        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        for(int h=1; h <= totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
                ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where trip =" + i +" and userid =" + h);
                ch.next();
                totalsegment = ch.getInt(1);

                for(int j=1; j <= totalsegment; j++)
                {
                    ResultSet ch5 = statement.executeQuery("select max(acceleration) from gpsp where userid =" + h + " and trip =" + i +" and segmentnumber=" + j);
                    ch5.next();
                    maxai = ch5.getDouble(1);

                    String sql = "UPDATE gpsp SET segmentmaxai=" + maxai + " where trip =" + i +" and userid =" + h + " and segmentnumber=" + j;
                    statement.executeUpdate(sql);
                    System.out.println("Maximum Acceleration of Segment " + j + " of trip " + i + " of userid " + h + " is " + maxai);
                }
            }
        }
    }

    public static void countAV() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaltrip;
        int totalsegment;
        int totaluser;
        double sumspatialdistance;
        double temporalinterval;
        double av;

        ResultSet ch0 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch0.next();
        totaluser = ch0.getInt(1);

        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        for(int h=1; h <= totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
                ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where trip =" + i +" and userid =" + h);
                ch.next();
                totalsegment = ch.getInt(1);
                for(int j=1; j <= totalsegment; j++)
                {
                    ResultSet ch5 = statement.executeQuery("select sum(spatialdistance) from gpsp where userid =" + h + " and trip =" + i +" and segmentnumber=" + j);
                    ch5.next();
                    sumspatialdistance = ch5.getDouble(1);

                    ResultSet rs = statement.executeQuery("select sum(temporalinterval) from gpsp where userid =" + h + " and trip =" + i +" and segmentnumber =" + j);
                    rs.next();
                    temporalinterval = rs.getDouble(1);

                    if(temporalinterval == 0.0)
                    {
                        temporalinterval = 0.01;
                    }

                    av = sumspatialdistance / temporalinterval;

                    String sql = "UPDATE gpsp SET segmentav=" + av + " WHERE userid =" + h + " and trip =" + i +" and segmentnumber=" + j;
                    statement.executeUpdate(sql);
                    System.out.println("Average Velocity of Segment " + j + " of trip " + i + " of userid " + h + " is " + av);

                }
            }
        }
    }

    public static void countEV() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect;
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaltrip;
        int totalsegment;
        int totaluser;
        int totalsubsegment;
        double ev;
        double sumvelocity;

        ResultSet ch0 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch0.next();
        totaluser = ch0.getInt(1);

        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        for(int h=1; h <= totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
                ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where trip =" + i +" and userid =" + h);
                ch.next();
                totalsegment = ch.getInt(1);

                for(int j=1; j <= totalsegment; j++)
                {
                    ResultSet ch5 = statement.executeQuery("select sum(velocity) from gpsp where userid =" + h + " and trip =" + i +" and segmentnumber=" + j);
                    ch5.next();
                    sumvelocity = ch5.getDouble(1);

                    ResultSet ch4 = statement.executeQuery("select count(distinct subsegmentid) from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j);
                    ch4.next();
                    totalsubsegment = ch4.getInt(1);

                    ev = sumvelocity/totalsubsegment;

                    String sql = "UPDATE gpsp SET segmentev=" + ev + " where trip =" + i +" and userid =" + h + " and segmentnumber=" + j;
                    statement.executeUpdate(sql);
                    System.out.println("Expectation of Velocity of Segment " + j + " of trip " + i + " of userid " + h + " is " + ev);
                }
            }
        }
    }

    public static void countDV() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaltrip;
        int totalsegment;
        int totaluser;
        int totalsubsegment;
        double av;
        double dv;
        double temp = 0;
        double velocity;

        ResultSet ch0 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch0.next();
        totaluser = ch0.getInt(1);

        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        for(int h=6; h <= totaluser; h++)
        {
            for(int i=5; i <= totaltrip; i++)
            {
                ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where trip =" + i +" and userid =" + h);
                ch.next();
                totalsegment = ch.getInt(1);
                for(int j=1; j <= totalsegment; j++)
                {
                    ResultSet ch4 = statement.executeQuery("select count(distinct subsegmentid) from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j);
                    ch4.next();
                    totalsubsegment = ch4.getInt(1);

                    for(int k=1; k <= totalsubsegment; k++)
                    {
                        ResultSet ch5 = statement.executeQuery("select segmentav from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j + " limit 1");
                        ch5.next();
                        av = ch5.getDouble(1);

                        ResultSet ch6 = statement.executeQuery("select velocity from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j + " and subsegmentid=" + k);
                        ch6.next();
                        velocity = ch6.getDouble(1);

                        temp += (velocity - av) * (velocity - av);
                    }
                    dv = temp / totalsubsegment;

                    String sql = "UPDATE gpsp SET segmentdv=" + dv + " WHERE trip =" + i +" and userid =" + h + " and segmentnumber=" + j;
                    statement.executeUpdate(sql);
                    System.out.println("Variance of Velocity of Segment " + j + " of trip " + i + " of userid " + h + " is " + dv);
                }
            }
        }
    }

    public static void countHCR() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaltrip;
        int totalsegment;
        int totaluser;
        int totalsubsegment;
        int hc;
        double hcr;
        int totalhc = 0;
        double segmentdistance;

        ResultSet ch0 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch0.next();
        totaluser = ch0.getInt(1);

        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        for(int h=4; h <= totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
                ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where trip =" + i +" and userid =" + h);
                ch.next();
                totalsegment = ch.getInt(1);
                for(int j=1; j <= totalsegment; j++)
                {
                    ResultSet ch4 = statement.executeQuery("select count(distinct subsegmentid) from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j);
                    ch4.next();
                    totalsubsegment = ch4.getInt(1);
                    for(int k=1; k <= totalsubsegment; k++)
                    {
                        ResultSet ch5 = statement.executeQuery("select headingchange from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j + " and subsegmentid=" + k);
                        ch5.next();
                        hc = ch5.getInt(1);
                        if(hc > 19)
                        {
                            totalhc++;
                        }
                    }
                    ResultSet ch6 = statement.executeQuery("select segmentdist from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j + " limit 1");
                    ch6.next();
                    segmentdistance = ch6.getDouble(1);
                    if(segmentdistance == 0)
                    {
                        segmentdistance = 0.0001;
                    }
                    hcr = totalhc / segmentdistance;

                    String sql = "UPDATE gpsp SET hcr=" + hcr + " WHERE trip =" + i +" and userid =" + h + " and segmentnumber=" + j;
                    statement.executeUpdate(sql);
                    System.out.println("Heading Change Rate of Segment " + j + " of trip " + i + " of userid " + h + " is " + hcr);
                }
            }
        }
    }

    public static void countSR() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaltrip;
        int totalsegment;
        int totaluser;
        int totalsubsegment;
        double sr;
        int s;
        int totals = 0;
        double segmentdistance;

        ResultSet ch0 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch0.next();
        totaluser = ch0.getInt(1);

        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);

        for(int h=1; h <= totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
                ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where trip =" + i +" and userid =" + h);
                ch.next();
                totalsegment = ch.getInt(1);
                for(int j=1; j <= totalsegment; j++)
                {
                    ResultSet ch4 = statement.executeQuery("select count(distinct subsegmentid) from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j);
                    ch4.next();
                    totalsubsegment = ch4.getInt(1);
                    for(int k=1; k <= totalsubsegment; k++)
                    {
                        ResultSet ch5 = statement.executeQuery("select velocity from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j + " and subsegmentid=" + k);
                        ch5.next();
                        s = ch5.getInt(1);
                        if(s > 19)
                        {
                            totals++;
                        }
                    }

                    ResultSet ch6 = statement.executeQuery("select segmentdist from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j + " limit 1");
                    ch6.next();
                    segmentdistance = ch6.getDouble(1);
                    if(segmentdistance == 0)
                    {
                        segmentdistance = 0.0001;
                    }
                    sr = totals / segmentdistance;
                    String sql = "UPDATE gpsp SET sr=" + sr + " where trip =" + i +" and userid =" + h + " and segmentnumber=" + j;
                    statement.executeUpdate(sql);
                    System.out.println("Stop Rate of Segment " + j + " of trip " + i + " of userid " + h + " is " + sr);
                }
            }
        }
    }

    public static void countVCR() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();

        int totaltrip;
        int totalsegment;
        int totaluser;
        int totalsubsegment;
        double vcr;
        int totalv = 0;
        double segmentdistance;
        double vrate;
        double velocity1;
        double velocity2;


        ResultSet ch0 = statement.executeQuery("select count(distinct userid) from gpsp");
        ch0.next();
        totaluser = ch0.getInt(1);

        ResultSet ch1 = statement.executeQuery("select count(distinct trip) from gpsp");
        ch1.next();
        totaltrip = ch1.getInt(1);



        for(int h=1; h <= totaluser; h++)
        {
            for(int i=1; i <= totaltrip; i++)
            {
                ResultSet ch = statement.executeQuery("select count(distinct segmentnumber) from gpsp where trip =" + i +" and userid =" + h);
                ch.next();
                totalsegment = ch.getInt(1);
                for(int j=1; j <= totalsegment; j++)
                {
                    ResultSet ch4 = statement.executeQuery("select count(distinct subsegmentid) from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j);
                    ch4.next();
                    totalsubsegment = ch4.getInt(1);
                    for(int k=1; k <= totalsubsegment; k++)
                    {
                        if(k == totalsubsegment)
                        {
                            vrate = 0;
                        }
                        else
                        {
                            ResultSet ch5 = statement.executeQuery("select velocity from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j + " and subsegmentid=" + k);
                            ch5.next();
                            velocity1 = ch5.getInt(1);

                            ResultSet ch6 = statement.executeQuery("select velocity from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j + " and subsegmentid=" + (k+1));
                            ch6.next();
                            velocity2 = ch6.getInt(1);

                            vrate = Math.abs(velocity2 - velocity1) / velocity1;
                        }

                        if(vrate > 0.26)
                        {
                            totalv++;
                        }
                    }

                    ResultSet ch6 = statement.executeQuery("select segmentdist from gpsp where trip =" + i +" and userid =" + h + " and segmentnumber=" + j + " limit 1");
                    ch6.next();
                    segmentdistance = ch6.getDouble(1);
                    if(segmentdistance == 0)
                    {
                        segmentdistance = 0.0001;
                    }
                    vcr = totalv / segmentdistance;

                    String sql = "UPDATE gpsp SET vcr=" + vcr + " WHERE trip =" + i +" and userid =" + h + " and segmentnumber=" + j;
                    statement.executeUpdate(sql);
                    System.out.println("Velocity Change Rate of Segment " + j + " of userid " + h + " is " + vcr);
                }
            }
        }
    }
}
