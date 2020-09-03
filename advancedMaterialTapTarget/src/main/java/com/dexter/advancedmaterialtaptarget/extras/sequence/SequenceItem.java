/*
 * Copyright (C) 2016-2018 Samuel Wall
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dexter.advancedmaterialtaptarget.extras.sequence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dexter.advancedmaterialtaptarget.AdvancedMaterialTapTargetPrompt;
import com.dexter.advancedmaterialtaptarget.AdvancedMaterialTapTargetSequence;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a prompt to display in a sequence.
 */
public class SequenceItem implements AdvancedMaterialTapTargetPrompt.PromptStateChangeListener
{
    /**
     * Defines the prompt in this sequence item.
     */
    @NonNull
    private final SequenceState sequenceState;

    /**
     * Lists the state changes that trigger this sequence item to complete.
     */
    @NonNull
    final List<Integer> stateChangers = new ArrayList<>();

    /**
     * Listener for this sequence item completing.
     */
    @Nullable
    private AdvancedMaterialTapTargetSequence.SequenceCompleteListener sequenceListener;

    /**
     * Constructor.
     *
     * @param state The prompt that this item will show.
     */
    public SequenceItem(@NonNull final SequenceState state)
    {
        this.sequenceState = state;
    }

    /**
     * Add a state that will trigger the sequence to move on.
     *
     * @see AdvancedMaterialTapTargetPrompt#STATE_REVEALING
     * @see AdvancedMaterialTapTargetPrompt#STATE_REVEALED
     * @see AdvancedMaterialTapTargetPrompt#STATE_FOCAL_PRESSED
     * @see AdvancedMaterialTapTargetPrompt#STATE_FINISHING
     * @see AdvancedMaterialTapTargetPrompt#STATE_FINISHED
     * @see AdvancedMaterialTapTargetPrompt#STATE_NON_FOCAL_PRESSED
     * @see AdvancedMaterialTapTargetPrompt#STATE_DISMISSING
     * @see AdvancedMaterialTapTargetPrompt#STATE_DISMISSED
     *
     * @param state The state that triggers the sequence to move on.
     */
    public void addStateChanger(final int state)
    {
        this.stateChangers.add(state);
    }

    /**
     * Remove a specific state changer.
     *
     * @see AdvancedMaterialTapTargetPrompt#STATE_REVEALING
     * @see AdvancedMaterialTapTargetPrompt#STATE_REVEALED
     * @see AdvancedMaterialTapTargetPrompt#STATE_FOCAL_PRESSED
     * @see AdvancedMaterialTapTargetPrompt#STATE_FINISHING
     * @see AdvancedMaterialTapTargetPrompt#STATE_FINISHED
     * @see AdvancedMaterialTapTargetPrompt#STATE_NON_FOCAL_PRESSED
     * @see AdvancedMaterialTapTargetPrompt#STATE_DISMISSING
     * @see AdvancedMaterialTapTargetPrompt#STATE_DISMISSED
     *
     * @param state The state to remove.
     */
    public void removeStateChanger(final int state)
    {
        this.stateChangers.remove((Integer) state);
    }

    /**
     * Remove all state changers.
     */
    public void clearStateChangers()
    {
        this.stateChangers.clear();
    }

    /**
     * Set the listener for this sequence item completing.
     *
     * @param listener The item finish listener.
     */
    public void setSequenceListener(@Nullable final AdvancedMaterialTapTargetSequence.SequenceCompleteListener listener)
    {
        this.sequenceListener = listener;
    }

    /**
     * Get the prompt state that this sequence item uses.
     *
     * @return The prompt state.
     */
    @NonNull
    public SequenceState getState()
    {
        return this.sequenceState;
    }

    /**
     * Show this sequence item.
     */
    public void show()
    {
        final AdvancedMaterialTapTargetPrompt prompt = this.sequenceState.getPrompt();
        if (prompt != null)
        {
            this.show(prompt);
        }
        else
        {
            this.onItemComplete();
        }
    }

    /**
     * Calls {@link AdvancedMaterialTapTargetPrompt#finish()} on this items states prompt.
     */
    public void finish()
    {
        final AdvancedMaterialTapTargetPrompt prompt = this.sequenceState.getPrompt();
        if (prompt != null)
        {
            prompt.finish();
        }
    }

    /**
     * Calls {@link AdvancedMaterialTapTargetPrompt#dismiss()} on this items states prompt.
     */
    public void dismiss()
    {
        final AdvancedMaterialTapTargetPrompt prompt = this.sequenceState.getPrompt();
        if (prompt != null)
        {
            prompt.dismiss();
        }
    }

    /**
     * Show the created prompt for this sequence item.
     *
     * @param prompt The prompt to show, this will never be null here.
     */
    protected void show(@NonNull final AdvancedMaterialTapTargetPrompt prompt)
    {
        prompt.show();
    }

    @Override
    public void onPromptStateChanged(@NonNull final AdvancedMaterialTapTargetPrompt prompt, final int state)
    {
        if (this.stateChangers.contains(state))
        {
            this.onItemComplete();
        }
    }

    /**
     * Emits the {@link AdvancedMaterialTapTargetSequence.SequenceCompleteListener#onSequenceComplete()} event if the listener
     * is set.
     */
    protected void onItemComplete()
    {
        if (this.sequenceListener != null)
        {
            this.sequenceListener.onSequenceComplete();
        }
    }
}
