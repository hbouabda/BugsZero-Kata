package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    public static final int MAX_PLAYERS = 6;
    private List<String> players;
    private int[] places;
    private int[] purses;
    private boolean[] inPenaltyBox;

    private Map<String, LinkedList<String>> categoryQuestions;

    private int currentPlayer = 0;
    private boolean isGettingOutOfPenaltyBox;

    public Game () {
        categoryQuestions = new HashMap<>();
        players = new ArrayList();

        Arrays.stream(Category.values()).forEach(category -> {
            categoryQuestions.put(category.label, new LinkedList<>());
        });

        for (int i = 0; i < 50; i++) {
            for (Category category : Category.values()) {
                categoryQuestions.get(category.label).add(createQuestion(i, category.label));
            }
        }

        places = new int[MAX_PLAYERS];
        purses = new int[MAX_PLAYERS];
        inPenaltyBox = new boolean[MAX_PLAYERS];
    }

    private static String createQuestion(int i, String category) {
        return category + " Question " + i;
    }

    public boolean isPlayable() {
        return howManyPlayers() <= MAX_PLAYERS;
    }

    public boolean add(String playerName) {

        if (!isPlayable()) {
            System.out.println("le nombre de joueur dépasse " + MAX_PLAYERS);
            return false;
        }

        players.add(playerName);
        places[howManyPlayers() - 1] = 0;
        purses[howManyPlayers() - 1] = 0;
        inPenaltyBox[howManyPlayers() - 1] = false;

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + howManyPlayers());
        return true;
    }

    public int howManyPlayers() {
        return players.size();
    }

    public void roll(int roll) {
        System.out.println(players.get(currentPlayer) + " is the current player");
        System.out.println("They have rolled a " + roll);
        isGettingOutOfPenaltyBox = roll % 2 != 0;

        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
                movePlayerAndAskQuestion(roll);
            } else {
                System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
            }

        } else {
            movePlayerAndAskQuestion(roll);
        }

    }

    private void movePlayerAndAskQuestion(int roll) {
        places[currentPlayer] = places[currentPlayer] + roll;
        if (places[currentPlayer] > 11) {
			places[currentPlayer] = places[currentPlayer] - 12;
		}

        System.out.println(players.get(currentPlayer)
                + "'s new location is "
                + places[currentPlayer]);
        System.out.println("The category is " + currentCategory());
        askQuestion();
    }

    private void askQuestion() {
        String category = currentCategory();
        String question;
        switch (category) {
            case "Pop":
                question = categoryQuestions.get("Pop").removeFirst();
            case "Science":
                question = categoryQuestions.get("Science").removeFirst();
            case "Sports":
                question = categoryQuestions.get("Sports").removeFirst();
            default:
                question = categoryQuestions.get("Rock").removeFirst();
        }
        System.out.println(question);
    }


    private String currentCategory() {
        int player = places[currentPlayer];
        switch (player) {
            case 0:
            case 4:
            case 8:
                return "Pop";
            case 1:
            case 5:
            case 9:
                return "Science";
            case 2:
            case 6:
            case 10:
                return "Sports";
            default:
                return "Rock";
        }
    }

    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                System.out.println("Answer was correct!!!!");
				goToNextPlayer();
				purses[currentPlayer]++;
                System.out.println(players.get(currentPlayer)
                        + " now has "
                        + purses[currentPlayer]
                        + " Gold Coins.");

                boolean winner = didPlayerWin();

                return winner;
            } else {
				goToNextPlayer();
                return true;
            }


        } else {

            System.out.println("Answer was corrent!!!!");
            purses[currentPlayer]++;
            System.out.println(players.get(currentPlayer)
                    + " now has "
                    + purses[currentPlayer]
                    + " Gold Coins.");

            boolean winner = didPlayerWin();
			goToNextPlayer();

            return winner;
        }
    }

	private void goToNextPlayer() {
		currentPlayer++;
		if (currentPlayer == players.size()) {
			currentPlayer = 0;
		}
	}

	public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
        inPenaltyBox[currentPlayer] = true;

		goToNextPlayer();
        return true;
    }


    private boolean didPlayerWin() {
        return purses[currentPlayer] != 6;
    }
}
