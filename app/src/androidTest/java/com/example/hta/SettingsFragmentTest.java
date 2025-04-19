package com.example.hta;

import android.content.Intent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.espresso.contrib.PickerActions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

    @Rule
    public ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class, true, false);

    @Test
    public void testSetThresholds() {
        Intent intent = new Intent();
        intent.putExtra("FRAGMENT_NAME", SettingsFragment.class.getName());
        activityRule.launchActivity(intent);

        // Digite valores nos campos de entrada
      /*  onView(withId(R.id.upperThreshold)).perform(typeText("150"));
        onView(withId(R.id.lowerThreshold)).perform(typeText("70"));

        // Clique no botão para definir os limites
        onView(withId(R.id.setThresholdsButton)).perform(click());*/

        // Verifique se a mensagem de sucesso é exibida
        onView(withId(R.id.successMessage)).check(matches(withText("Limites definidos!")));
        onView(withId(R.id.successMessage)).check(matches(isDisplayed()));
    }
}
