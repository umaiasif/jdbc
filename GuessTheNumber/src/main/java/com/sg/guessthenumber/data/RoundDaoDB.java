/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.data;

import com.sg.guessthenumber.dto.Game;
import com.sg.guessthenumber.dto.Round;
import java.util.List;

/**
 *
 * @author umair
 */
public interface RoundDaoDB {
    List<Round> getRounds(int gameId);
    
    Round getRoundById(int roundId);
    
    Round addRound(Round round);
}
