package com.ndtv.mediaprima.mvvm.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by Elaa on 9/14/2016.
 */
public class Search extends Item {

    public List<Data> data;

    public class Data implements ParentListItem {
        public List<SectionItems> section_items;

        public String section_type;

        public String section_name;

        @Override
        public List<SectionItems> getChildItemList() {
            return section_items;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return true;
        }
    }

}

