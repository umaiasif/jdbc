/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.service;

import com.sg.guessthenumber.data.GameDaoDB;
import com.sg.guessthenumber.data.RoundDaoDB;
import com.sg.guessthenumber.dto.Game;
import com.sg.guessthenumber.dto.Round;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author umair
 */
@Service
public class ServiceLayer {

    @Autowired
    GameDaoDB gameDao;

    @Autowired
    RoundDaoDB roundDao;

    public List<Game> getAll() {
        List<Game> games = gameDao.getAll();
        for (Game game : games) {
            if (!game.isStatus()) {
                game.setAnswer("****");
            }
        }

        return games;
    }

    public List<Round> getRounds(int gameId) {
        return roundDao.getRounds(gameId);
    }

    public Round makeGuess(Round round) {
        String answer = gameDao.findById(round.getGameId()).getAnswer();
        String guess = round.getGuessNum();
        String result = determineResult(guess, answer);
        round.setResult(result);
        
        if (guess.equals(answer)) {
            Game game = findById(round.getGameId());
            game.setStatus(true);
            gameDao.updateGame(game);
        }
        
        return roundDao.addRound(round);
    }

    public Game findById(int gameId) {
        Game game = gameDao.findById(gameId);
        if (!game.isStatus()) {
            game.setAnswer("****");
        }

        return game;
    }

    public Game addGame(Game game) {
        return gameDao.add(game);
    }

    public int newGame() {
        Game game = new Game();
        game.setAnswer(generateAnswer());
        game = gameDao.add(game);

        return game.getGameId();
    }

    private String generateAnswer() {
        Random rnd = new Random();
        int digit1 = rnd.nextInt(10);

        int digit2 = rnd.nextInt(10);
        while (digit2 == digit1) {
            digit2 = rnd.nextInt(10);
        }

        int digit3 = rnd.nextInt(10);
        while (digit3 == digit2 || digit3 == digit1) {
            digit3 = rnd.nextInt(10);
        }

        int digit4 = rnd.nextInt(10);
        while (digit4 == digit3 || digit4 == digit2 || digit4 == digit1) {
            digit4 = rnd.nextInt(10);
        }

        String answer = String.valueOf(digit1) + String.valueOf(digit2)
                + String.valueOf(digit3) + String.valueOf(digit4);

        return answer;
    }

    public String determineResult(String guess, String answer) {
        char[] guessChars = guess.toCharArray();
        char[] answerChars = answer.toCharArray();
        int exact = 0;
        int partial = 0;
        
        for (int i = 0; i < guessChars.length; i++) {
            // -1 indicates that index value of guessChars DNE in answer
            // otherwise the number must be in the string. Then check where
            if (answer.indexOf(guessChars[i]) > 0) {
                if (guessChars[i] == answerChars[i]) {
                    exact++;
                } else {
                    partial++;
                }
            }
        }
        
        String result = "e:" + exact + ":p:" + partial;
        
        return result;
    }

}
