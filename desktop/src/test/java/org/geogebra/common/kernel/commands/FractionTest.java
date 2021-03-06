package org.geogebra.common.kernel.commands;

import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.main.App;
import org.geogebra.test.TestErrorHandler;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FractionTest {

	private static App app;

	@BeforeClass
	public static void setup() {
		app = AlgebraTest.createApp();
	}

	@Test
	public void symbolicFractionsCAS() {
		t("a=2/3-1/3", "1 / 3");
		t("Simplify(x/3/a)", "x");
		t("Simplify(x^a)", "cbrt(x)");
		t("Simplify(a!)", "1 / 3 * gamma(1 / 3)");
	}

	@Test
	public void functionWithFractions() {
		t("frac(x)=(3/2)^x", "(3 / 2)^(x)");
		t("frac(2)", "9 / 4");
		t("frac(-1)", "2 / 3");
		t("frac(-2)", "4 / 9");
	}

	@Test
	public void scientificNotation() {
		t("5*10^(-2)", "1 / 20");
	}

	private static void t(String string, String string2) {
		GeoElementND[] geos = app.getKernel().getAlgebraProcessor()
				.processAlgebraCommandNoExceptionHandling(string, false,
						TestErrorHandler.INSTANCE,
						new EvalInfo(true).withFractions(true), null);
		Assert.assertEquals(string2,
				geos[0].toValueString(StringTemplate.testTemplate));
	}
}
