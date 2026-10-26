
package com.PrepMate

import android.content.Intent
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

class QuestionSelectionFragment : Fragment() {

    private var subject = ""
    private var code = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_question_selection,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Navigation drawer
        val drawerLayout =
            view.findViewById<DrawerLayout>(R.id.drawerLayout)

        val menuButton =
            view.findViewById<ImageButton>(R.id.menuButton)

        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Welcome message
        val prefs = requireActivity()
            .getSharedPreferences("PrepMatePrefs", MODE_PRIVATE)

        val name = prefs.getString("userName", null)

        val welcomeText =
            view.findViewById<TextView>(R.id.welcomeText)

        welcomeText.text = "Welcome, $name!"

        // Bookmarked Questions menu item
        val bookmarkedQuestionsOption =
            view.findViewById<TextView>(
                R.id.bookmarkedQuestionsOption
            )

        bookmarkedQuestionsOption.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)

            startActivity(
                Intent(
                    requireActivity(),
                    BookmarksActivity::class.java
                )
            )
        }

        // Attempt Analysis menu item
        val attemptAnalysisOption =
            view.findViewById<TextView>(
                R.id.attemptAnalysisOption
            )

        attemptAnalysisOption.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)

            startActivity(
                Intent(
                    requireActivity(),
                    AttemptAnalysisActivity::class.java
                )
            )
        }

        // Subject buttons
        val dsaButton =
            view.findViewById<LinearLayout>(R.id.dsaButton)

        val dbmsButton =
            view.findViewById<LinearLayout>(R.id.dbmsButton)

        val osButton =
            view.findViewById<LinearLayout>(R.id.osButton)

        val cnButton =
            view.findViewById<LinearLayout>(R.id.cnButton)

        val oopButton =
            view.findViewById<LinearLayout>(R.id.oopButton)

        val coaButton =
            view.findViewById<LinearLayout>(R.id.coaButton)

        val daaButton =
            view.findViewById<LinearLayout>(R.id.daaButton)

        val seButton =
            view.findViewById<LinearLayout>(R.id.seButton)

        // DSA
        dsaButton.setOnClickListener {
            subject = "Data Structures and Algorithms"
            code = "DSA"
            openConfig()
        }

        // DBMS
        dbmsButton.setOnClickListener {
            subject = "Database Management Systems"
            code = "DBMS"
            openConfig()
        }

        // OS
        osButton.setOnClickListener {
            subject = "Operating Systems"
            code = "OS"
            openConfig()
        }

        // Computer Networks
        cnButton.setOnClickListener {
            subject = "Computer Networks"
            code = "CN"
            openConfig()
        }

        // OOP
        oopButton.setOnClickListener {
            subject = "Object Oriented Programming"
            code = "OOP in Java"
            openConfig()
        }

        // COA
        coaButton.setOnClickListener {
            subject = "Computer Organization and Architecture"
            code = "COA"
            openConfig()
        }

        // DAA
        daaButton.setOnClickListener {
            subject = "Design and Analysis of Algorithms"
            code = "DAA"
            openConfig()
        }

        // Software Engineering
        seButton.setOnClickListener {
            subject = "Software Engineering"
            code = "Software Engineering"
            openConfig()
        }
    }

    private fun openConfig() {
        GlobalData.subject = subject
        GlobalData.code = code

        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainerView,
                TestConfigFragment()
            )
            .addToBackStack(null)
            .commit()
    }
}
