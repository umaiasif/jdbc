/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.data;

import com.sg.guessthenumber.dto.Game;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class GameDaoDBImplementation implements GameDaoDB{
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Game add(Game game) {
        final String INSERT_GAME = "INSERT INTO game(answer) VALUES(?)";
        jdbc.update(INSERT_GAME, game.getAnswer());
        
        int newGameId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        game.setGameId(newGameId);
        return game;
               
    }
  

    @Override
    public List<Game> getAll() {
        final String SELECT_ALL_GAMES = "SELECT * FROM game";
        return jdbc.query(SELECT_ALL_GAMES, new GameMapper());
    }

    @Override
    public Game findById(int gameId) {
        try{
            final String SELECT_FIND_BY_ID = "SELECT * FROM game WHERE gameId=?";
            return jdbc.queryForObject(SELECT_FIND_BY_ID, new GameMapper(), gameId);
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    public void updateGame(Game game) {
        final String UPDATE_GAME = "UPDATE game SET currentStatus = ?";
        jdbc.update(UPDATE_GAME, game.isStatus(), game.getGameId());
    }
    
 
    public static final class GameMapper implements RowMapper<Game> {
        
        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setGameId(rs.getInt("gameId"));
            game.setAnswer(rs.getString("answer"));
            game.setStatus(rs.getBoolean("currentStatus"));
            return game;
        }

    
    }
    
}
