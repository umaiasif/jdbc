/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.data;

import com.sg.guessthenumber.dto.Round;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author umair
 */
@Repository
public class RoundDaoDBImplementation implements RoundDaoDB {

    
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public List<Round> getRounds(int gameId) {
        try{
            final String SELECT_ROUNDS_BY_GAMEID = "SELECT * FROM round WHERE gameId = ? ORDER BY timeOfGuess";
            List<Round> rounds = jdbc.query(SELECT_ROUNDS_BY_GAMEID, new RoundMapper(), gameId);
            return rounds;
        } catch(DataAccessException ex) {
            return null;
        }
           
        
    }

    @Override
    public Round getRoundById(int roundId) {
       try{
           final String SELECT_ROUND_BY_ID = "SELECT * FROM round WHERE roundId = ?";
           return jdbc.queryForObject(SELECT_ROUND_BY_ID, new RoundMapper(), roundId);
       }
       catch(DataAccessException ex){
           return null;
       }
    }

    @Override
    @Transactional
    public Round addRound(Round round) {
        final String INSERT_ROUND = "INSERT INTO round(gameId, guessNum, result)";
        jdbc.update(INSERT_ROUND, round.getGameId(), round.getGuessNum(), round.getResult());
        
        int newRoundId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        round.setRoundId(newRoundId);
        return getRoundById(newRoundId);
     
    }
    
   public static final class RoundMapper implements RowMapper<Round> {
        
        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setRoundId(rs.getInt("roundId"));
            round.setGameId(rs.getInt("gameId"));
            round.setGuessNum(rs.getString("guessNum"));
            
            Timestamp timestamp = rs.getTimestamp("timeOfGuess");
            round.setGuessTime(timestamp.toLocalDateTime());
            
            round.setResult(rs.getString("result"));
            return round;
        }
    }
   
}
