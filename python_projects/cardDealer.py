#Tyler Boechler

import random as r

class Card(object):
    def __init__(self):
        """
        Purpose:
            Initialize a Card object instance.
        """
        self.__cards = []
        self.__key = {1:"A", 11:"J", 12:"Q", 13:"K", "A":1, "J":11, "Q":12, "K":13} # key to convert letter cards to numbers and vice versa
        
    def create(self):
        """
        Purpose:
            Creates a deck of cards as a list
        Pre-conditions:
            None
        Post-conditions:
            None
        Returns:
            A deck of cards in a list
        """
        #iterate through 52 times and make cards as strings
        
        suits = ["H", "D", "C", "S"]
        for s in suits:
            for i in range(1, 14):
                if i in self.__key:
                    self.__cards.append(self.__key[i]+s)  #use key to turn numbers into letter cards
                else:
                    self.__cards.append(str(i)+s)
                
        return self.__cards
            
        
    def deal(self, num_cards, num_players, deck):
        """
        Purpose:
            Deals out a hand of cards with a specific amount of people and cards from a deck
        Pre-conditions:
            param num_cards: int value of the number of cards to be dealt
            param num_players: int value of the number of players
            param deck: list of cards to be dealt
        Post-conditions:
            cards in deck will be removed until hands are full or deck is empty
        Returns:
            A list of lists of hands of cards from the deck
        """
        #if the deck dosent exist or isnt possible return none
        if num_cards == 0 or num_players == 0 or len(deck) == 0:
            return None
        
        #blank hands
        players_hands = []
        for p in range(num_players):
                    players_hands.append([])
        
        #iterate through the cards to be dealt in amount of cards then the players
        for c in range(num_cards):
                #are there enough cards for the next round?
                if not len(deck) >= num_players:
                    #Deal remaining cards
                    for player in range(len(deck)):
                        players_hands[player].append(deck.pop(r.randint(0, len(deck)-1)))
                        
                    return players_hands
                
                #Deal hand of cards
                for player in players_hands:
                    player.append(deck.pop(r.randint(0, len(deck)-1)))
                    
        return players_hands
        
        
    def value(self, card):
        """
        Purpose:
            Return the number value of an inputted card
        Pre-conditions:
            param card: the card's value to be determined
        Post-conditions:
            None
        Returns:
            The integer value of the card
            None if card is None
        """
        
        if card == None:
            return None
        
        #Return number value of card
        if card[0] in self.__key:
            return self.__key[card[0]] #use key for letter carsd
        return int(card[:-1])
        
        
    def highest(self, list_of_cards):
        """
        Purpose:
            Returns the highest card of a list of cards
        Pre-conditions:
            param list_of_cards: a list of cards who's highest is tbd
        Post-conditions:
            None
        Returns:
            The highest string of the card in the list
            None if list is empty
        """
        
        if len(list_of_cards) == 0:
            return None
        
        largest = 0
        card = 0
        #Finding largest number value of cards by number value
        for c in list_of_cards:
            if c[0] in self.__key:
                card = self.__key[c[0]]
            else:
                card = int(c[:-1])
            if card > largest:
                largest = card
                #returning actual card string
                largest_card = c
                
        return largest_card
                
        
    def lowest(self, list_of_cards):
        """
        Purpose:
            Returns the lowest card of a list of cards
        Pre-conditions:
            param list_of_cards: a list of cards who's lowest is tbd
        Post-conditions:
            None
        Returns:
            The lowest string of the card in the list
            None if list is empty
        """
        
        if len(list_of_cards) == 0:
            return None
        
        smallest = 14
        card = 0
        #finding smallest of cards in list by number values
        for c in list_of_cards:
            if c[0] in self.__key:
                card = self.__key[c[0]]
            else:
                card = int(c[:-1])
            if card < smallest:
                smallest = card
                #returning actual card value
                smallest_card = c
                
        return smallest_card
        
    def average(self, list_of_cards):
        """
        Purpose:
            Returns the average number value of a list of cards
        Pre-conditions:
            param list_of_cards: the list of cards to calculate the avg
        Post-conditions:
            None
        Returns:
            The average value of the cards as a float
            None if list is empty
        """
        
        if len(list_of_cards) == 0:
            return None
        
        cards = []
        
        #Getting number values for cards
        for c in list_of_cards:
            if c[0] in self.__key:
                cards.append(self.__key[c[0]])
            else:
                cards.append(int(c[:-1]))
                
        return sum(cards)/len(cards)


