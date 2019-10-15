package seedu.ezwatchlist.model;

import org.junit.jupiter.api.Test;
import seedu.ezwatchlist.model.UserPrefs;

import static seedu.ezwatchlist.testutil.Assert.assertThrows;

public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setWatchListFilePath(null));
    }

}