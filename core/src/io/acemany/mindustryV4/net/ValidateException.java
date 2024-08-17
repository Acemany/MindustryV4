package io.acemany.mindustryV4.net;

import io.acemany.mindustryV4.entities.Player;

/**
 * Thrown when a client sends invalid information.
 */
public class ValidateException extends RuntimeException{
    public final Player player;

    public ValidateException(Player player, String s){
        super(s);
        this.player = player;
    }
}
