import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View {

    private JFrame _frame;
    private JPanel _container;
    private JPanel _statusPanel;
    private JPanel _panel;
    private JButton _connectButton;
    private JLabel _statusLabel;
    private JButton[][] _buttonArray = new JButton[8][8];

    public JLabel get_statusLabel()
    {
        return this._statusLabel;
    }

    public View()
    {
        _frame = new JFrame("Checkers Game");
        _panel = new JPanel();
        _container = new JPanel();
        _statusPanel = new JPanel();
        _connectButton = new JButton("Connect");
        _statusLabel = new JLabel("");

        //set up the board
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                _buttonArray[i][j] = new JButton();
                _buttonArray[i][j].setPreferredSize(new Dimension(80,80));
                _buttonArray[i][j].setFocusPainted(false);
                _buttonArray[i][j].setForeground(Color.BLACK);
                if (((i-j)%2) == 0)
                {
                    _buttonArray[i][j].setBackground(Color.CYAN);
                }
                else
                {
                    _buttonArray[i][j].setBackground(Color.WHITE);
                }
                _panel.add(_buttonArray[i][j]);
            }
        }

        _container.setLayout(new BoxLayout(_container, BoxLayout.Y_AXIS));
        _connectButton.setFocusPainted(false);
        _connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.connectButtonClicked();
            }
        });

        _statusPanel.add(_connectButton);
        _statusPanel.add(_statusLabel);
        _container.add(_statusPanel);

        _panel.setLayout(new GridLayout(8, 8));
        _container.add(_panel);
        _frame.add(_container);
        _frame.pack();
        _frame.setLocationRelativeTo(null);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setVisible(true);
    }

    public void updateView(Board board)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                char currentChar = board.getBoard()[i][j];
                switch (currentChar) {
                    case '-':
                        _buttonArray[i][j].setText("");
                        _buttonArray[i][j].setIcon(null);
                        break;
                    case 'r':
                        try {
                            Image img = ImageIO.read(getClass().getResource("Images/red piece.jpg"));
                            img = img.getScaledInstance(50,50,Image.SCALE_DEFAULT);
                            _buttonArray[i][j].setIcon(new ImageIcon(img));
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        break;
                    case 'b':
                        try {
                            Image img = ImageIO.read(getClass().getResource("Images/black piece.jpg"));
                            img = img.getScaledInstance(50,50,Image.SCALE_DEFAULT);
                            _buttonArray[i][j].setIcon(new ImageIcon(img));
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        break;
                }
            }
        }

        _frame.pack();
    }

    public void removeConnectButton()
    {
        _statusPanel.remove(_connectButton);
    }

    public void changeStatus(String status)
    {
        _statusLabel.setText(status);
    }
}
