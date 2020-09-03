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

import androidx.annotation.Nullable;

import com.dexter.advancedmaterialtaptarget.AdvancedMaterialTapTargetPrompt;


/**
 * Stores a prompt for a {@link SequenceItem} in a sequence.
 */
public class SequenceState
{
    /**
     * The stored prompt.
     */
    @Nullable
    AdvancedMaterialTapTargetPrompt prompt;

    /**
     * Constructor.
     *
     * @param prompt The prompt to use in this state.
     */
    public SequenceState(@Nullable final AdvancedMaterialTapTargetPrompt prompt)
    {
        this.prompt = prompt;
    }

    /**
     * Get the stored prompt.
     *
     * @return The prompt.
     */
    @Nullable
    public AdvancedMaterialTapTargetPrompt getPrompt()
    {
        return this.prompt;
    }
}
