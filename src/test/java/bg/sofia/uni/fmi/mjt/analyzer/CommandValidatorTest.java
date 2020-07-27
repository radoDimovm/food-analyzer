package bg.sofia.uni.fmi.mjt.analyzer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommandValidatorTest {
    private CommandValidator validator = new CommandValidator();

    @Test
    public void testEmptyCommand() {
        assertFalse(validator.isCommandValid("    "));
        assertFalse(validator.isCommandValid("\n"));
        assertFalse(validator.isCommandValid(""));
    }

    @Test
    public void testInvalidCountOfArguments() {
        assertFalse(validator.isCommandValid("get-food"));
        assertFalse(validator.isCommandValid("get-food butter raffaello"));
        assertFalse(validator.isCommandValid("get-food-report"));
        assertFalse(validator.isCommandValid("get-food-report 0815305813 5305920358"));
        assertFalse(validator.isCommandValid("get-food-by-barcode --upc=0352986446 --img=C:\\user\\barcodeImg.jpg --upc=99473757576"));
    }

    @Test
    public void testCommandsWithTypos() {
        assertFalse(validator.isCommandValid("get-foood butter"));
        assertFalse(validator.isCommandValid("getfood raffaello"));
        assertFalse(validator.isCommandValid("get-food-reoirt 0815305813"));
        assertFalse(validator.isCommandValid("get-food-barcode --upc=0352986446"));
        assertFalse(validator.isCommandValid("get-food-by-barcode --ubc=0352986446"));
        assertFalse(validator.isCommandValid("get-food-by-barcode -img=C:\\user\\barcodeImg.jpg"));
    }

    @Test
    public void testRightCommands() {
        assertTrue(validator.isCommandValid("get-food butter"));
        assertTrue(validator.isCommandValid("get-food-report 4515305813"));
        assertTrue(validator.isCommandValid("get-food-by-barcode --upc=0352986446"));
        assertTrue(validator.isCommandValid("get-food-by-barcode --img=C:\\user\\barcodeImg.jpg"));
        assertTrue(validator.isCommandValid("get-food-by-barcode --img=C:\\user\\barcodeImg.jpg --upc=0352986446"));
    }
}
