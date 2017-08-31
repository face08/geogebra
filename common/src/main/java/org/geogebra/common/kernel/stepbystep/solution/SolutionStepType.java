package org.geogebra.common.kernel.stepbystep.solution;

import org.geogebra.common.kernel.stepbystep.steptree.StepNode;
import org.geogebra.common.main.GeoGebraColorConstants;
import org.geogebra.common.main.Localization;
import org.geogebra.common.util.StringUtil;

public enum SolutionStepType {
	WRAPPER("", ""),
	
	SIMPLIFICATION_WRAPPER("", ""),

	EQUATION("", ""),

	SOLVE("Solve", "Solve: %0"),

	NEW_CASE("CaseA", "Case ?: %0"),
	
	SOLVING_IN_INTERVAL("SolvingInInterval", "Case ?: %0 when %1"),

	CANT_SOLVE("CantSolve", "Cannot Solve"),

	TRUE_FOR_ALL("TrueForAllAInB", "The equation is true for all %0"),

	NO_REAL_SOLUTION("NoRealSolutions", "No Real Solutions"),

	SOLUTION("SolutionA", "Solution: %0"),

	SOLUTIONS("SolutionsA", "Solutions: %0") {
		@Override
		public String getDefaultText(Localization loc, StepNode[] parameters) {
			String serializedDefault = "";
			for (int i = 0; i < parameters.length; i++) {
				if (i != 0) {
					serializedDefault += ",\\;";
				}
				serializedDefault += parameters[i].toLaTeXString(loc, false);
			}
			return loc.getMenuLaTeX(getKey(), getDefault(), serializedDefault);
		}

		@Override
		public String getColoredText(Localization loc, int color, StepNode[] parameters) {
			String serializedColored = "";
			for (int i = 0; i < parameters.length; i++) {
				if (i != 0) {
					serializedColored += ",\\;";
				}
				serializedColored += parameters[i].toLaTeXString(loc, true);
			}
			return loc.getMenuLaTeX(getKey(), getDefault(), serializedColored) + colorText(color);
		}
	},

	CHECK_VALIDITY("CheckingValidityOfSolutions", "Checking validity of solutions"),

	VALID_SOLUTION("ValidSolution", "Valid Solution: %0"),

	INVALID_SOLUTION("InvalidSolution", "Invalid Solution: %0"),

	VALID_SOLUTION_ABS("ValidSolutionAbs", "%0 \\in %1"),

	INVALID_SOLUTION_ABS("ValidSolutionAbs", "%0 \\notin %1"),

	RESOLVE_ABSOLUTE_VALUES("ResolveAbsoluteValues", "Resolve Absolute Values"),

	SQUARE_ROOT("TakeSquareRoot", "Take square root of both sides"),

	CUBE_ROOT("TakeCubeRoot", "Take cube root of both sides"),

	NTH_ROOT("TakeNthRoot", "Take %0th root of both sides"),

	SQUARE_BOTH_SIDES("SquareBothSides", "Square both sides"),

	ADD_TO_BOTH_SIDES("AddAToBothSides", "Add %0 to both sides"),

	EXPAND_PARENTHESES("ExpandParentheses", "Expand Parentheses"),

	SUBTRACT_FROM_BOTH_SIDES("SubtractAFromBothSides", "Subtract %0 from both sides"),

	MULTIPLY_BOTH_SIDES("MultiplyBothSidesByA", "Multiply both sides by %0"),

	DIVIDE_BOTH_SIDES("DivideBothSidesByA", "Divide both sides by %0"),

	FACTOR_EQUATION("FactorEquation", "Factor equation"),
	
	USE_QUADRATIC_FORMULA("UseQuadraticFormulaWithABC", "Use quadratic formula with a = %0, b = %1, c = %2"),

	QUADRATIC_FORMULA("QuadraticFormula", "%0 = \\frac{-b \\pm \\sqrt{b^2-4ac}}{2a}"),

	COMPLETE_THE_CUBE("CompleteCube", "Complete the cube"),

	COMPLETE_THE_SQUARE("CompleteSquare", "Complete the square"),

	NO_SOLUTION_TRIGONOMETRIC("NoSolutionTrigonometricSin", "%0 \\in [-1, 1] for all %1 \\in \\mathbb{R}"),

	REPLACE_WITH("ReplaceAWithB", "Replace %0 with %1"),

	RATIONAL_ROOT_THEOREM("RationalRootTheorem",
			"A polynomial equation with integer coefficients has all of its rational roots in the form p/q, where p divides the constant term and q divides the coefficient of the highest order term"),

	EXPAND_FRACTIONS("ExpandFractions", "Expand Fractions, the common denominator is: %0"),

	FACTOR_DENOMINATORS("FatorDenominators", "Factor Denominators"),

	PRODUCT_IS_ZERO("ProductIsZero", "Product is zero"),

	TRIAL_AND_ERROR("TrialAndError", "Find the roots by trial and error, and factor them out"),

	SOLVE_NUMERICALLY("SolveNumerically", "Solve numerically: "),

	REGROUP_WRAPPER("RegroupExpression", "Regroup Expression"),

	DOUBLE_MINUS("DoubleMinus", "A double negative is a positive"),

	RATIONALIZE_DENOMINATOR("RationalizeDenominator", "Rationalize the denominator."),

	DISTRIBUTE_ROOT_FRAC("DistributeRootOverFraction", "Distribute the root over the fraction"),

	DISTRIBUTE_MINUS("DistributeMinus", "Distribute minus"),

	ADD_CONSTANTS("AddConstants", "Add constants"),

	COLLECT_LIKE_TERMS("CollectLikeTerms", "Collect like terms: %0"),

	ADD_FRACTIONS("AddFractions", "Add fractions"),

	MULTIPLY_CONSTANTS("MultiplyConstants", "Multiply constants"),

	COMMON_FRACTION("CommonFraction", "Write the product as a single fraction"),

	CANCEL_FRACTION("CancelFraction", "Cancel %0 in the fraction"),

	MULTIPLIED_BY_ZERO("MultipliedByZero", "Anything multiplied by zero is zero"),

	REGROUP_PRODUCTS("RegroupProducts", "Regroup products: "),

	SQUARE_MINUS("SquareMinus", "Squaring a minus makes it go awaaayy"),

	DIVIDE_ROOT_AND_POWER("DivideRootAndPower", "Divide the root and power by: "),

	DIVIDE_ROOT_AND_POWER_EVEN("DivideRootAndPowerEven", "Divide the root and power by: "),

	EVALUATE_POWER("EvaluatePower", "Evaluate power"),

	ZEROTH_POWER("ZerothPower", "The zeroth power of anything is one"),

	FIRST_POWER("FirstPower", "The first power of anything is itself"),

	FIRST_ROOT("FirstRoot", "The first root of anything is itself"),

	REWRITE_AS_POWER("RevriteAAsB", "Rewrite %0 as %1"),

	FACTOR_SQUARE("FactorSquare", "Factor out the perfect square"),

	EVALUATE_INVERSE_TRIGO("EvaluateInverseTrigo", "Evaluate inverse trigonometric function");
	
	private final String keyText;
	private final String defaultText;
	
	SolutionStepType(String keyText, String defaultText) {
		this.keyText = keyText;
		this.defaultText = defaultText;
	}
	
	public String getDefaultText(Localization loc, StepNode[] parameters) {
		if (parameters == null) {
			return loc.getMenuLaTeX(getKey(), getDefault());
		}

		String[] serializedDefault = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			serializedDefault[i] = parameters[i].toLaTeXString(loc, false);
		}
		return loc.getMenuLaTeX(getKey(), getDefault(), serializedDefault);
	}

	public String getColoredText(Localization loc, int color, StepNode[] parameters) {
		if (parameters == null) {
			return loc.getMenuLaTeX(getKey(), getDefault()) + colorText(color);
		}

		String[] serializedColored = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			serializedColored[i] = parameters[i].toLaTeXString(loc, true);
		}

		return loc.getMenuLaTeX(getKey(), getDefault(), serializedColored) + colorText(color);
	}

	public static String colorText(int color) {
		return color == 0 ? "" : "\\fgcolor{" + getColorHex(color) + "}{\\;\\bullet}";
	}

	private static String getColorHex(int color) {
		switch (color % 5) {
		case 1:
			return StringUtil.toHexString(GeoGebraColorConstants.GEOGEBRA_OBJECT_RED);
		case 2:
			return StringUtil.toHexString(GeoGebraColorConstants.GEOGEBRA_OBJECT_BLUE);
		case 3:
			return StringUtil.toHexString(GeoGebraColorConstants.GEOGEBRA_OBJECT_GREEN);
		case 4:
			return StringUtil.toHexString(GeoGebraColorConstants.GEOGEBRA_OBJECT_PURPLE);
		case 0:
			return StringUtil.toHexString(GeoGebraColorConstants.GEOGEBRA_OBJECT_ORANGE);
		default:
			return StringUtil.toHexString(GeoGebraColorConstants.GEOGEBRA_OBJECT_BLACK);
		}
	}

	public String getKey() {
		return keyText;
	}
	
	public String getDefault() {
		return defaultText;
	}
}
