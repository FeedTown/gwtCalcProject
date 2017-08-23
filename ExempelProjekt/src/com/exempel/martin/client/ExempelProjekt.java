package com.exempel.martin.client;

import java.util.ArrayList;
//import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
//import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class ExempelProjekt implements EntryPoint{

	private VerticalPanel mainPanel = new VerticalPanel();
	//private VerticalPanel designPanel = new VerticalPanel();
	private FlexTable calcFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox operand1TextBox = new TextBox();
	private TextBox operand2TextBox = new TextBox();
	private Button calculateButton = new Button("Calculate");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox operatorTextBox = new SuggestBox(oracle);
	private FocusPanel addPanelWithEH = new FocusPanel();
	private ArrayList<String> calcValues = new ArrayList<String>();

	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {

		// Create table for calculator
		calcFlexTable.setText(0, 0, "Question");
		calcFlexTable.setText(0, 1, "Answer");
		calcFlexTable.setText(0, 2, "Remove");

		// Suggests the valid operators
		oracle.add("*");
		oracle.add("%");
		oracle.add("+");
		// oracle.add("-");

		// Add styles to elements in the stock list table
		
		calcFlexTable.getRowFormatter().addStyleName(0, "calcListHeader");
		calcFlexTable.addStyleName("calcList");
		mainPanel.addStyleName("calcListPanel");

		operand1TextBox.setSize("50px", "20px");
		operatorTextBox.setSize("50px", "20px");
		operand2TextBox.setSize("50px", "20px");
		
		operand1TextBox.addStyleName("operatorTextBox");
		operatorTextBox.addStyleName("operatorTextBox");
		operand2TextBox.addStyleName("operatorTextBox");
		
		
		
		

		// Assemble add operatorbox to panel
		addPanel.add(operand1TextBox);
		addPanel.add(operatorTextBox);
		addPanel.add(operand2TextBox);
		addPanel.add(calculateButton);
		addPanelWithEH.add(addPanel);
		

		// Assemble Main panel.
		mainPanel.add(calcFlexTable);
		mainPanel.add(addPanelWithEH);
		
		
		// Associate the Main panel with the HTML host page.
		RootPanel.get("calc").add(mainPanel);
		
		//Move cursor focus to the input box.
		operand1TextBox.setFocus(true);
		
		
		eventHandler();

	}

	private void eventHandler() {
		calculateButton.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (event.getNativeButton() == 1)
					calculate();

			}

		});

		/*
		 * calculateButton.addClickHandler(new ClickHandler() { public void
		 * onClick(ClickEvent event) { calculate(); }});
		 */
		addPanelWithEH.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					calculate();
				}	
			}

					
		});
		

	}
	

	private void calculate() {

		final String operator = operatorTextBox.getText().trim();
		calculateButton.setFocus(true);
		if ((!operator.equals("*") && !operator.equals("+") && !operator.equals("%"))
				|| !isInteger(operand1TextBox.getText().trim()) || !isInteger(operand2TextBox.getText().trim())) {
			Window.alert("You have entered a non valid binary operator or one of the operands is not an integer");

			return;
		}

		int operand1 = Integer.parseInt(operand1TextBox.getText());
		int operand2 = Integer.parseInt(operand2TextBox.getText());
		int answer = 0;
		// Multiplication
		if (operator.equals("*")) {
			answer = multiplication(operand1, operand2, answer);
		}
		// Modulo
		else if (operator.equals("%")){
			answer = modulo(operand1, operand2, answer);

		}
		// Addition
		else 
		{
			answer = addition(operand1, operand2, answer);

		}
		
		
		addCalculateValuesToList(operand1,operand2,answer, operator);
		
		operand1TextBox.setText("");
		operand2TextBox.setText("");
		operatorTextBox.setText("");
		operand1TextBox.setFocus(true);
		
		

	}

	private void addCalculateValuesToList(int operand1, int operand2, int answer, String operator) {
		
		String operand1InString = Integer.toString(operand1);
		String operand2InString = Integer.toString(operand2);
		String answerToString = Integer.toString(answer);
		
		final String symbol = operand1InString + " " + operator + " " + operand2InString + " = ";
		
		/*if(calcValues.contains(symbol))
			return;
		*/
		
		int row = calcFlexTable.getRowCount();
		calcValues.add(symbol);
		calcFlexTable.setText(row, 0, symbol);
		calcFlexTable.setText(row, 1, answerToString);
		
		
		//Add a button to remove this stock from the table.
		   Button removeStockButton = new Button("x");
		   removeStockButton.addStyleDependentName("remove");
		    removeStockButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        int removedIndex = calcValues.indexOf(symbol);
		        calcValues.remove(removedIndex);
		        calcFlexTable.removeRow(removedIndex + 1);
		      }
		    });
		    calcFlexTable.setWidget(row, 2, removeStockButton);
		
		
		
	}

	private int multiplication(int operand1, int operand2, int answer) {
		// TODO Auto-generated method stub
		answer = operand1 * operand2;
		
		Window.alert("The answer is: " + answer);
		
		return answer;

	}

	private int modulo(int operand1, int operand2, int answer) {
		// TODO Auto-generated method stub
		answer = operand1 % operand2;
		Window.alert("The answer is: " + answer);
		
		return answer;

	}

	private int addition(int operand1, int operand2, int answer) {
		// TODO Auto-generated method stub
		answer = operand1 + operand2;
		Window.alert("The answer is: " + answer);
		
		return answer;

	}
	
	
	// Checkes if a String could be seen as an integer
	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}