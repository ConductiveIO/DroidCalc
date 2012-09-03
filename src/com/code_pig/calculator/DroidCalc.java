package com.code_pig.calculator;

import java.util.concurrent.Callable;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * DroidCal: The simple android calculator. Built to learn.
 * @author Robby Grodin
 *
 */

public class DroidCalc extends Activity {

	private String EMPTY_FIELD = "";
	
	// Storage for input, operator and operands for the current equation.
	private String input = EMPTY_FIELD;
	private Float op1 = null;
	private Float op2 = null;
	private Callable<Float> operator = null;
	
	EditText opView;
	EditText numView;

	ButtonClickListener ClickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create ClickListener and attach to each button.
		ButtonClickListener ClickListener = new ButtonClickListener();
		int idList[] = { R.id.button0, R.id.button1, R.id.button2,
				R.id.button3, R.id.button4, R.id.button5, R.id.button6,
				R.id.button7, R.id.button8, R.id.button9, R.id.buttonPlus,
				R.id.buttonMinus, R.id.buttonDiv, R.id.buttonMult,
				R.id.buttonE, R.id.buttonC, R.id.buttonD };

		for (int id : idList) {
			View v = findViewById(id);
			v.setOnClickListener(ClickListener);
		}

		// Set EditText view variables
		opView = (EditText) findViewById(R.id.opField);
		numView = (EditText) findViewById(R.id.calcField);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Calculator Entry Methods
	 */
	
	// Clears all stored values and displays
	private void clear() {
		input = EMPTY_FIELD;
		operator = null;
		op1 = null;
		op2 = null;
		opView.setText(EMPTY_FIELD);
		numView.setText(EMPTY_FIELD);
	}
	
	// processes the current operands with the current operator
	private void compute() {
		if (operator != null && op1 != null) {
			if (input.length() > 0) {
				try {
					op2 = Float.parseFloat(input);
					input = EMPTY_FIELD;
					
					Float result = operator.call();
					numView.setText(result.toString());
				} catch (Exception e) {
					// This should never happen, due to the logic check above.
					e.printStackTrace();
				}
			} else {
				try {
					op2 = op1;
					Float result = operator.call();
					numView.setText(result.toString());
					op1 = result;
					op2 = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Appends the value to the current input
	private void enterNumber(CharSequence pressed) {
		input += (String) pressed;
		numView.setText(input);
	}

	// Changes the operator and runs the previous operation
	private void enterOp() throws Exception {
		boolean isDirty = input.length() > 0;
		if (isDirty && op1 != null) {
			op2 = Float.parseFloat(input);
			float result = operator.call();
			op1 = result;
			input = EMPTY_FIELD;
			numView.setText(Float.toString(result));
		} else if (isDirty) {
			op1 = Float.parseFloat(input);
			input = EMPTY_FIELD;
		}
	}

	/**
	 * Operators: Creates and stores a Callable object to calculate a result.
	 */
	
	private void enterPlus() {
		operator = new Callable<Float>() {
			public Float call() throws Exception {
				float result = op1 + op2;
				op1 = result;
				op2 = null;
				return result;
			}
		};
	}

	private void enterMinus() {
		operator = new Callable<Float>() {
			public Float call() throws Exception {
				float result = op1 - op2;
				op1 = result;
				op2 = null;
				return result;
			}
		};
	}

	private void enterMult() {
		operator = new Callable<Float>() {
			public Float call() throws Exception {
				float result = op1 * op2;
				op1 = result;
				op2 = null;
				return result;
			}
		};
	}

	private void enterDiv() {
		operator = new Callable<Float>() {
			public Float call() {
				float result = op1 / op2;
				op1 = result;
				op2 = null;
				return result;
			}
		};
	}

	// Click listening class to report button clicks.
	class ButtonClickListener implements OnClickListener {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonE:
				compute();
				break;

			case R.id.buttonC:
				clear();
				break;

			case R.id.buttonPlus:
				try {
					enterOp();
					enterPlus();
					opView.setText("+");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case R.id.buttonMinus:
				try {
					enterOp();
					enterMinus();
					opView.setText("-");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case R.id.buttonMult:
				try {
					enterOp();
					enterMult();
					opView.setText("x");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case R.id.buttonDiv:
				try {
					enterOp();
					enterDiv();
					opView.setText("/");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			default:
				System.out.println(((Button) v).getText());
				enterNumber(((Button) v).getText());
			}
		}
	}
}
