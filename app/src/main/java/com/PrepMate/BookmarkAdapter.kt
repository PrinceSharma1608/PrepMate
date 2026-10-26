package com.PrepMate

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.PrepMate.data.local.BookmarkedQuestion
import com.google.android.material.button.MaterialButton

class BookmarkAdapter(
    private val bookmarks: MutableList<BookmarkedQuestion>,
    private val onRemove: (BookmarkedQuestion) -> Unit
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    class BookmarkViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val questionNumber: TextView = view.findViewById(R.id.bookmarkQuestionNumber)

        val questionText: TextView = view.findViewById(R.id.bookmarkQuestionText)

        val optionsGroup: RadioGroup = view.findViewById(R.id.bookmarkOptionsGroup)

        val optionA: RadioButton = view.findViewById(R.id.bookmarkOptionA)

        val optionB: RadioButton = view.findViewById(R.id.bookmarkOptionB)

        val optionC: RadioButton = view.findViewById(R.id.bookmarkOptionC)

        val optionD: RadioButton = view.findViewById(R.id.bookmarkOptionD)

        val removeButton: MaterialButton = view.findViewById(R.id.removeBookmarkButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.bookmark_card,
                parent,
                false
            )

        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmark = bookmarks[position]

        holder.questionNumber.text = "Question ${bookmark.qid}"
        holder.questionText.text = bookmark.question

        holder.optionA.text = bookmark.optionA
        holder.optionB.text = bookmark.optionB
        holder.optionC.text = bookmark.optionC
        holder.optionD.text = bookmark.optionD

        // Enable options so user can choose an answer
        holder.optionsGroup.isEnabled = true
        holder.optionA.isEnabled = true
        holder.optionB.isEnabled = true
        holder.optionC.isEnabled = true
        holder.optionD.isEnabled = true

        // Reset colors
        val defaultTint = ColorStateList.valueOf(Color.parseColor("#6B8F71"))
        val defaultTextColor = Color.parseColor("#304536")

        holder.optionA.buttonTintList = defaultTint
        holder.optionB.buttonTintList = defaultTint
        holder.optionC.buttonTintList = defaultTint
        holder.optionD.buttonTintList = defaultTint

        holder.optionA.setTextColor(defaultTextColor)
        holder.optionB.setTextColor(defaultTextColor)
        holder.optionC.setTextColor(defaultTextColor)
        holder.optionD.setTextColor(defaultTextColor)

        holder.optionsGroup.setOnCheckedChangeListener(null)
        holder.optionsGroup.clearCheck()

        holder.optionsGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val selectedRadio = group.findViewById<RadioButton>(checkedId)
                val selectedText = selectedRadio.text.toString()

                val greenColor = Color.parseColor("#1B5E20")
                val redColor = Color.parseColor("#C62828")

                if (selectedText == bookmark.correctAnswer) {
                    selectedRadio.buttonTintList = ColorStateList.valueOf(greenColor)
                    selectedRadio.setTextColor(greenColor)
                } else {
                    selectedRadio.buttonTintList = ColorStateList.valueOf(redColor)
                    selectedRadio.setTextColor(redColor)

                    val correctRadio = when (bookmark.correctAnswer) {
                        bookmark.optionA -> holder.optionA
                        bookmark.optionB -> holder.optionB
                        bookmark.optionC -> holder.optionC
                        bookmark.optionD -> holder.optionD
                        else -> null
                    }
                    correctRadio?.buttonTintList = ColorStateList.valueOf(greenColor)
                    correctRadio?.setTextColor(greenColor)
                }
            }
        }

        holder.removeButton.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition

            if (currentPosition != RecyclerView.NO_POSITION) {
                onRemove(bookmarks[currentPosition])
            }
        }
    }

    override fun getItemCount(): Int = bookmarks.size

    fun removeItem(bookmark: BookmarkedQuestion) {
        val index = bookmarks.indexOfFirst {
            it.qid == bookmark.qid
        }

        if (index != -1) {
            bookmarks.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
