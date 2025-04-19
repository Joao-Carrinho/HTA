package com.example.hta;

import android.content.Intent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class InputFragmentTest {

    @Rule
    public ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class, true, false);

    @Test
    public void testInputValues() {
        Intent intent = new Intent();
        intent.putExtra("FRAGMENT_NAME", InputFragment.class.getName());
        activityRule.launchActivity(intent);

        // Verificar se o valor foi inserido corretamente
        onView(withId(R.id.inputNumber1)).check(matches(withText("")));
        onView(withId(R.id.inputNumber2)).check(matches(withText("")));
    }
}
