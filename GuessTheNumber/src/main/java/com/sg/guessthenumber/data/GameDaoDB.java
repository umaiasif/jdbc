/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.data;

import com.sg.guessthenumber.dto.Game;
import java.util.List;

/**
 *
 * @author umair
 */
public interface GameDaoDB {
    Game add(Game game);
    
    List<Game> getAll();
    
    Game findById(int gameID);
    
    void updateGame(Game game);
    
    
}
