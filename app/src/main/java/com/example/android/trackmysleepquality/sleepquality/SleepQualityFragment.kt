/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleepquality

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.databinding.FragmentSleepQualityBinding

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepQualityFragment : Fragment() {

    private lateinit var binding: FragmentSleepQualityBinding

    private val application: Application
        get() = requireNotNull(this.activity).application

    private val arguments: SleepQualityFragmentArgs
        get() = SleepQualityFragmentArgs.fromBundle(requireNotNull(getArguments()))

    private val dataSource: SleepDatabaseDao
        get() = SleepDatabase.getInstance(application).sleepDatabaseDao

    private val viewModel: SleepQualityViewModel by viewModels {
        SleepQualityViewModelFactory(arguments.sleepNightKey, dataSource)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.navigateToSleepTracker.observe(viewLifecycleOwner) {
            if (it == true) {
                this.findNavController().navigate(
                    SleepQualityFragmentDirections
                        .actionSleepQualityFragmentToSleepTrackerFragment()
                )
                viewModel.doneNavigating()
            }
        }

        binding = FragmentSleepQualityBinding.inflate(inflater)

        binding.sleepQualityViewModel = viewModel

        return binding.root
    }
}
