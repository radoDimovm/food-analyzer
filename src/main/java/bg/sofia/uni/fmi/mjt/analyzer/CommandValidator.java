package bg.sofia.uni.fmi.mjt.analyzer;

public class CommandValidator {
    public boolean isCommandValid(String command) {
        if (command.strip().isEmpty()) {
            return false;
        }

        String[] commandTokens = command.strip().split("\\s+");

        if (commandTokens.length < 2) {
            return false;
        }

        String commandWord = commandTokens[0];

        if (commandWord.equals("get-food-by-barcode")) {
            if (commandTokens.length == 3 &&
                    ((commandTokens[1].substring(0, 6).equals("--upc=") && commandTokens[2].substring(0, 6).equals("--img=")) ||
                            (commandTokens[1].substring(0, 6).equals("--img=") && commandTokens[2].substring(0, 6).equals("--upc=")))) {
                return true;
            } else if (commandTokens[1].substring(0, 6).equals("--upc=") || commandTokens[1].substring(0, 6).equals("--img=")) {
                return true;
            }
        } else if (commandWord.equals("get-food-report") && commandTokens.length == 2) {
            return true;
        } else if (commandWord.equals("get-food")) {
            return true;
        }

        return false;
    }
}
