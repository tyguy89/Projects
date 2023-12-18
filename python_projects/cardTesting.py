#Tyler Boechler

from cardDealer import Card as C

#provided function
def close_enough(a, b, tolerance):
    """
    Purpose:
        Check if 2 floating point values are close enough to 
        be considered equal. 
    Pre-Conditions:
        :param a: a floating point value
        :param b: a floating point value
        :param tolerance: a small positive floating point value
    Post-Conditions:
        none
    Return:
        :return True if the difference between a and b is small
    """
    
    return abs(a - b) < tolerance

#TESTING

#Testing Card.create()
test_item = 'create()'
deck = ['AH', '2H', '3H', '4H', '5H', '6H', '7H', '8H', '9H', '10H', 'JH', 'QH', 'KH', 'AD', '2D', '3D', '4D', '5D', '6D', '7D', '8D', '9D', '10D', 'JD', 'QD', 'KD', 'AC', '2C', '3C', '4C', '5C', '6C', '7C', '8C', '9C', '10C', 'JC', 'QC', 'KC', 'AS', '2S', '3S', '4S', '5S', '6S', '7S', '8S', '9S', '10S', 'JS', 'QS', 'KS']
reason = "This is a deck of cards"

# call the operation
card = C()

result = card.create()

if result != deck:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))


#-----------------------------------------------
#testing Card.deal()
# call the operation
test_item = 'create(), deal()'


card = C()

result = card.deal(4, 4, card.create())
occurances = {}

for values in result:
    for cards in values or card not in deck:
        if cards in occurances:
            print('Error in {}: expected {} but obtained duplicate or card not in deck  {} --'.format(test_item, expected, result))
            break
        else:  
            occurances[cards] = 1

card = C()

result = card.deal(4, 13, card.create())
occurances = {}

for values in result:
    for cards in values or card not in deck:
        if cards in occurances:
            print('Error in {}: expected {} but obtained duplicate or card not in deck  {} -- {}'.format(test_item, expected, result, reason))
            break
        else:  
            occurances[cards] = 1

card = C()

result = card.deal(5, 11, card.create())
occurances = {}

for values in result:
    for cards in values or card not in deck:
        if cards in occurances:
            print('Error in {}: expected {} but obtained duplicate or card not in deck  {} -- {}'.format(test_item, expected, result, reason))
            break
        else:  
            occurances[cards] = 1

#-----------------------------------------------
#testing Card.value()

test_item = 'create()'
expected = 1
reason = "The value does not match cards"

# call the operation
card = C()

result = card.value("AH")

if result != expected:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))

test_item = 'create()'
expected = 5
reason = "The value does not match cards"

# call the operation
card = C()

result = card.value("5D")

if result != expected:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))

test_item = 'create()'
expected = 10
reason = "The value does not match cards"

# call the operation
card = C()

result = card.value("10H")

if result != expected:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))

#----------------------------------------
#testing Card.highest()

test_item = 'highest()'
expected = "KH"
reason = "The value does not match cards"

# call the operation
card = C()

result = card.highest(["KH", "AH", "6D"])

if result != expected:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))


test_item = 'highest()'
expected = "AD"
reason = "The value does not match cards"

# call the operation
card = C()

result = card.highest(["AD"])

if result != expected:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))


test_item = 'highest()'
expected = "10D"
reason = "The value does not match cards"

# call the operation
card = C()

result = card.highest(["4H", "AH", "6D", "10D"])

if result != expected:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))

test_item = 'highest()'
expected = None
reason = "The value does not match cards"

# call the operation
card = C()

result = card.highest([])

if expected != result:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))

#----------------------------------------
#testing Card.lowest()

test_item = 'lowest()'
expected = "AH"
reason = "The value does not match cards"

# call the operation
card = C()

result = card.lowest(["KH", "AH", "6D"])

if result != expected:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))


test_item = 'lowest()'
expected = "AD"
reason = "The value does not match cards"

# call the operation
card = C()

result = card.lowest(["AD"])

if result != expected:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))


test_item = 'lowest()'
expected = "4H"
reason = "The value does not match cards"

# call the operation
card = C()

result = card.lowest(["4H", "6D", "10D"])

if result != expected:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))

test_item = 'lowest()'
expected = None
reason = "The value does not match cards"

# call the operation
card = C()

result = card.lowest([])

if expected != result:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))

#---------------------------------
#testing Card.average()


test_item = 'average()'
expected = 6.666666666
reason = "The value does not match cards"

# call the operation
card = C()

result = card.average(["4H", "6D", "10D"])

if not close_enough(expected, result, 0.0001):
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))


test_item = 'average()'
expected = 5
reason = "The value does not match cards"

# call the operation
card = C()

result = card.average(["AH", "AD", "KD"])

if not close_enough(expected, result, 0.0001):
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))


test_item = 'average()'
expected = None
reason = "The value does not match cards"

# call the operation
card = C()

result = card.average([])

if expected != result:
    print('Error in {}: expected {} but obtained {} -- {}'.format(test_item, expected, result, reason))

print('*** Test script completed ***')

