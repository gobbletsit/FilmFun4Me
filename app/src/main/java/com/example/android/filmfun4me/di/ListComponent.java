package com.example.android.filmfun4me.di;

import com.example.android.filmfun4me.activity.activity.list.view.ListFragment;

import javax.inject.Inject;

import dagger.Provides;
import dagger.Subcomponent;

/**
 * Created by gobov on 12/22/2017.
 */

@ListScope
@Subcomponent(modules = ListModule.class)
public interface ListComponent {

    ListFragment inject(ListFragment fragment);

}
