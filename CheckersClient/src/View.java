import sun.awt.image.ImageWatched;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class View {

	private JFrame _frame;
	private JPanel _container;
	private JPanel _statusPanel;
	private JPanel _panel;
	private JButton _aboutButton;
	private JButton _connectButton;
	private JButton _submitMoveButton;
	private JButton _clearButton;
	private JButton _helpButton;
	private JLabel _statusLabel;
	private JButton[][] _buttonArray;

	public View()
	{
		_buttonArray = new JButton[8][8];
		_frame = new JFrame("Checkers Game");
		_panel = new JPanel();
		_container = new JPanel();
		_statusPanel = new JPanel();
		_aboutButton = new JButton("About");
		_connectButton = new JButton("Connect");
		_submitMoveButton = new JButton("Submit Move");
		_clearButton = new JButton("Clear Move");
		_helpButton = new JButton("Help");
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
					_buttonArray[i][j].setBackground(Color.BLACK);
				}
				else
				{
					_buttonArray[i][j].setBackground(Color.WHITE);
				}
				_buttonArray[i][j].addActionListener(e -> {
					Point buttonIndex = findButtonLocation((JButton) e.getSource());
					Client.gameButtonClicked(buttonIndex);
				});
				_panel.add(_buttonArray[i][j]);
			}
		}

		//set the top part of the UI
		_container.setLayout(new BoxLayout(_container, BoxLayout.Y_AXIS));
		_connectButton.setFocusPainted(false);
		_aboutButton.setFocusPainted(false);

		_aboutButton.addActionListener(e -> Client.aboutButtonClicked());
		_connectButton.addActionListener(e -> Client.connectButtonClicked());
		_clearButton.addActionListener(e -> Client.clearButtonClicked());
		_submitMoveButton.addActionListener(e -> Client.submitButtonClicked());
		_helpButton.addActionListener(e -> Client.helpButtonClicked());

		_statusPanel.add(_connectButton);
		_statusPanel.add(_statusLabel);
		_statusPanel.add(_submitMoveButton);
		_statusPanel.add(_clearButton);
		_statusPanel.add(_aboutButton);
		_statusPanel.add(_helpButton);
		_container.add(_statusPanel);

		_panel.setLayout(new GridLayout(8, 8));
		_container.add(_panel);
		_frame.add(_container);
		_frame.pack();
		_frame.setLocationRelativeTo(null);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setVisible(true);

		_frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				Client.sendForfeit();
			}
		});
	}

	//Find which button was clicked and return the index of it as a Point
	public Point findButtonLocation(JButton button)
	{
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (_buttonArray[i][j] == button)
				{
					return new Point(i,j);
				}
			}
		}
		return null;
	}

	public Color getPositionBGColor(Point p){
		return _buttonArray[p.x][p.y].getBackground();
	}

	public void setPositionBGColor(Point p, Color c){
		 _buttonArray[p.x][p.y].setBackground(c);
	}

	//Updates the UI with the latest board
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
					case 'R':
						try {
							Image img = ImageIO.read(getClass().getResource("Images/redking piece.jpg"));
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
					case 'B':
						try {
							Image img = ImageIO.read(getClass().getResource("Images/blackking piece.jpg"));
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

	//hide connect button since you can connect only once
	public void hideConnectButton()
	{
		this._connectButton.setVisible(false);
	}

	public void showConnectButton()
	{
		this._connectButton.setVisible(true);
	}

	public boolean submitButtonIsVisible() {
		return this._submitMoveButton.isVisible();
	}

	public void hideMoveButtons() {
		this._submitMoveButton.setVisible(false);
		this._clearButton.setVisible(false);
	}

	public void showMoveButtons()
	{
		this._submitMoveButton.setVisible(true);
		this._clearButton.setVisible(true);
	}

	public void hideAboutButton() {
		this._aboutButton.setVisible(false);
		this._aboutButton.setVisible(false);
	}

	public void showAboutButton() {
		this._aboutButton.setVisible(true);
		this._aboutButton.setVisible(true);
	}

	public void hideHelpButton() {
		this._helpButton.setVisible(false);
		this._helpButton.setVisible(false);
	}


	public void showHelpButton() {
		this._helpButton.setVisible(true);
		this._helpButton.setVisible(true);
	}

	public void hideClearButton() {
		this._clearButton.setVisible(false);
		this._clearButton.setVisible(false);
	}


	public void showClearButton() {
		this._clearButton.setVisible(true);
		this._clearButton.setVisible(true);
	}


	//change status of the UI at the top
	public void changeStatus(String status)
	{
		_statusLabel.setText(status);
	}

	public void showMessage(String s, String title) {
		JOptionPane.showMessageDialog(_frame, s, title, JOptionPane.PLAIN_MESSAGE);
	}

}
