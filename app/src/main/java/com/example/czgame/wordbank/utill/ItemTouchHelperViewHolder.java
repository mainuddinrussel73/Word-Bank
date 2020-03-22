package com.example.czgame.wordbank.utill;

public interface ItemTouchHelperViewHolder {

    void onItemSelected();
    /*
    Called when completed the move or swipe, and the active item * state should be cleared.
    */
    void onItemClear();
}