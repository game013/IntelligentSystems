# busters.py
# ----------
# Licensing Information: Please do not distribute or publish solutions to this
# project. You are free to use and extend these projects for educational
# purposes. The Pacman AI projects were developed at UC Berkeley, primarily by
# John DeNero (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and Pieter 
# Abbeel in Spring 2013.
# For more info, see http://inst.eecs.berkeley.edu/~cs188/pacman/pacman.html

from game import GameStateData
from game import Game
from game import Directions
from game import Actions
from game import Configuration
from util import nearestPoint
from util import manhattanDistance
import sys, util, types, time, random, layout, os
import random



########################################
# Parameters for noisy sensor readings #
########################################

def numberToTuple(number):
    data = []
    for action in [Directions.NORTH, Directions.SOUTH, Directions.EAST, Directions.WEST]:
        data.append( ( action, number % 2 == 0 ) )
        number = number / 2

    return tuple(data)

EPSILON = 0.1
WALL_VALUES = [ numberToTuple(n) for n in xrange(16) ]


#TODO: Create the distribution
"""
This method receives a noisy perception of the wall,
it is represented using a list of tuples in the following form:
(Direction,isWall) where Direction can be north, west, or east and isWall
and isWall is a boolean value that is True if there is wall in the given
direction.
"""
observationDistributionsWalls = {}
def getObservationDistributionNoisyWall(noisyWall):
    """
    Returns the probability P( noisyWall | truePerception )
    for every possible true perception. The ouput must be a Counter
    indexed by all the possible perceptions (contained in the list WALL_VALUES).
    Check the contents of WALL_VALUES to see how the noisy perception noisyWall and 
    the true perceptions are represented.
    """
    global observationDistributionsWalls
    if noisyWall == None:
        return util.Counter()

    if noisyWall not in observationDistributionsWalls:
        tmp = util.Counter()
        for wv in WALL_VALUES:
            val = 1
            for k in noisyWall:
                val *= 1 - EPSILON if k in wv else EPSILON
            tmp[wv] = val
        tmp.normalize()
        observationDistributionsWalls[noisyWall] = tmp

    return observationDistributionsWalls[noisyWall]

###################################################
# YOUR INTERFACE TO THE PACMAN WORLD: A GameState #
###################################################

class GameState:
    """
    A GameState specifies the full game state, including the food, capsules,
    agent configurations and score changes.

    GameStates are used by the Game object to capture the actual state of the game and
    can be used by agents to reason about the game.

    Much of the information in a GameState is stored in a GameStateData object.  We
    strongly suggest that you access that data via the accessor methods below rather
    than referring to the GameStateData object directly.

    Note that in classic Pacman, Pacman is always agent 0.
    """

    ####################################################
    # Accessor methods: use these to access state data #
    ####################################################

    def getLegalActions( self, agentIndex=0 ):
        """
        Returns the legal actions for the agent specified.
        """
        if self.isWin() or self.isLose(): return []

        if agentIndex == 0:  # Pacman is moving
            return PacmanRules.getLegalActions( self )
        else:
            return GhostRules.getLegalActions( self, agentIndex )

    def generateSuccessor( self, agentIndex, action):
        """
        Returns the successor state after the specified agent takes the action.
        """
        # Check that successors exist
        if self.isWin() or self.isLose(): raise Exception('Can\'t generate a successor of a terminal state.')

        # Copy current state
        state = GameState(self)

        # Let agent's logic deal with its action's effects on the board
        if agentIndex == 0:  # Pacman is moving
            state.data._eaten = [False for i in range(state.getNumAgents())]
            PacmanRules.applyAction( state, action )
        else:                # A ghost is moving
            GhostRules.applyAction( state, action, agentIndex )

        # Time passes
        if agentIndex == 0:
            state.data.scoreChange += -TIME_PENALTY # Penalty for waiting around
        else:
            GhostRules.decrementTimer( state.data.agentStates[agentIndex] )

        # Resolve multi-agent effects
        GhostRules.checkDeath( state, agentIndex )

        # Book keeping
        state.data._agentMoved = agentIndex
        state.data.score += state.data.scoreChange
        p = state.getPacmanPosition()

        if agentIndex == self.getNumAgents() - 1:
            state.numMoves += 1
        return state

    def getLegalPacmanActions( self ):
        return self.getLegalActions( 0 )

    def generatePacmanSuccessor( self, action ):
        """
        Generates the successor state after the specified pacman move
        """
        return self.generateSuccessor( 0, action )

    def getPacmanState( self ):
        """
        Returns an AgentState object for pacman (in game.py)

        state.pos gives the current position
        state.direction gives the travel vector
        """
        return self.data.agentStates[0].copy()

    def getPacmanPosition( self ):
        return self.data.agentStates[0].getPosition()

    def getNumAgents( self ):
        return len( self.data.agentStates )

    def getScore( self ):
        return self.data.score

    def getCapsules(self):
        """
        Returns a list of positions (x,y) of the remaining capsules.
        """
        return self.data.capsules

    def getNumFood( self ):
        return self.data.food.count()

    def getFood(self):
        """
        Returns a Grid of boolean food indicator variables.

        Grids can be accessed via list notation, so to check
        if there is food at (x,y), just call

        currentFood = state.getFood()
        if currentFood[x][y] == True: ...
        """
        return self.data.food

    def getWalls(self):
        """
        Returns a Grid of boolean wall indicator variables.

        Grids can be accessed via list notation, so to check
        if there is food at (x,y), just call

        walls = state.getWalls()
        if walls[x][y] == True: ...
        """
        return self.data.layout.walls

    def hasFood(self, x, y):
        return self.data.food[x][y]

    def hasWall(self, x, y):
        return self.data.layout.walls[x][y]

    ##############################
    # Additions for Busters Pacman #
    ##############################

    def getLivingGhosts(self):
        """
        Returns a list of booleans indicating which ghosts are not yet captured.

        The first entry (a placeholder for Pacman's index) is always False.
        """
        return self.livingGhosts

    def setGhostNotLiving(self, index):
        self.livingGhosts[index] = False

    def isLose( self ):
        return self.maxMoves > 0 and self.numMoves >= self.maxMoves

    def isWin( self ):
        return self.livingGhosts.count(True) == 0



    def getTrueWallPerception(self,location):
        p = []
        x,y = location
        for action in [Directions.NORTH, Directions.SOUTH, Directions.EAST, Directions.WEST]:
            dx, dy = Actions.directionToVector(action)
            nextx, nexty = int(x + dx), int(y + dy)
            p.append( ( action,self.hasWall( nextx, nexty ) ) )

        return tuple(p)

    def getNoisyWalls(self):
        position = self.getPacmanPosition()
        perception = []
        for action in [Directions.NORTH, Directions.SOUTH, Directions.EAST, Directions.WEST]:
            x,y = position
            dx, dy = Actions.directionToVector(action)
            nextx, nexty = int(x + dx), int(y + dy)
            isWall = self.hasWall(nextx,nexty)

            if util.flipCoin(EPSILON): isWall = not isWall

            perception.append( ( action, isWall ) )

        return tuple(perception)

    def getBoardWidth(self):
        return self.getWalls().width

    def getBoardHeight(self):
        return self.getWalls().height



    #############################################
    #             Helper methods:               #
    # You shouldn't need to call these directly #
    #############################################

    def __init__( self, prevState = None ):
        """
        Generates a new state by copying information from its predecessor.
        """
        if prevState != None:
            self.data = GameStateData(prevState.data)
            self.livingGhosts = prevState.livingGhosts[:]
            self.numMoves = prevState.numMoves;
            self.maxMoves = prevState.maxMoves;
        else: # Initial state
            self.data = GameStateData()
            self.numMoves = 0;
            self.maxMoves = -1;


    def deepCopy( self ):
        state = GameState( self )
        state.data = self.data.deepCopy()

        return state

    def __eq__( self, other ):
        """
        Allows two states to be compared.
        """
        return self.data == other.data

    def __hash__( self ):
        """
        Allows states to be keys of dictionaries.
        """
        return hash( str( self ) )

    def __str__( self ):

        return str(self.data)

    def initialize( self, layout, numGhostAgents=1000 ):
        """
        Creates an initial game state from a layout array (see layout.py).
        """
        self.data.initialize(layout, numGhostAgents)
        self.livingGhosts = [False] + [True for i in range(numGhostAgents)]

    def getGhostPosition( self, agentIndex ):
        if agentIndex == 0:
            raise "Pacman's index passed to getGhostPosition"
        return self.data.agentStates[agentIndex].getPosition()

    def getGhostState( self, agentIndex ):
        if agentIndex == 0:
            raise "Pacman's index passed to getGhostPosition"
        return self.data.agentStates[agentIndex]

############################################################################
#                     THE HIDDEN SECRETS OF PACMAN                         #
#                                                                          #
# You shouldn't need to look through the code in this section of the file. #
############################################################################

COLLISION_TOLERANCE = 0.7 # How close ghosts must be to Pacman to kill
TIME_PENALTY = 1 # Number of points lost each round

class BustersGameRules:
    """
    These game rules manage the control flow of a game, deciding when
    and how the game starts and ends.
    """

    def newGame( self, layout, pacmanAgent, ghostAgents, display, maxMoves= -1 ):
        agents = [pacmanAgent] + ghostAgents
        initState = GameState()
        initState.initialize( layout, len(ghostAgents))
        game = Game(agents, display, self)
        game.state = initState
        game.state.maxMoves = maxMoves
        return game

    def process(self, state, game):
        """
        Checks to see whether it is time to end the game.
        """
        if state.isWin(): self.win(state, game)
        if state.isLose(): self.lose(state, game)

    def win( self, state, game ):
        game.gameOver = True

    def lose( self, state, game ):
        game.gameOver = True

class PacmanRules:
    """
    These functions govern how pacman interacts with his environment under
    the classic game rules.
    """
    def getLegalActions( state ):
        """
        Returns a list of possible actions.
        """
        return Actions.getPossibleActions( state.getPacmanState().configuration, state.data.layout.walls )
    getLegalActions = staticmethod( getLegalActions )

    def applyAction( state, action ):
        """
        Edits the state to reflect the results of the action.
        """
        legal = PacmanRules.getLegalActions( state )
        if action not in legal:
            #return
            action = Directions.STOP
            #raise "Illegal action", action

        pacmanState = state.data.agentStates[0]

        # Update Configuration
        vector = Actions.directionToVector( action, 1)
        pacmanState.configuration = pacmanState.configuration.generateSuccessor( vector )

    applyAction = staticmethod( applyAction )

class GhostRules:
    """
    These functions dictate how ghosts interact with their environment.
    """
    def getLegalActions( state, ghostIndex ):
        conf = state.getGhostState( ghostIndex ).configuration
        return Actions.getPossibleActions( conf, state.data.layout.walls )
    getLegalActions = staticmethod( getLegalActions )

    def applyAction( state, action, ghostIndex):
        legal = GhostRules.getLegalActions( state, ghostIndex )
        if action not in legal:
            raise Exception("Illegal ghost action: " + str(action))

        ghostState = state.data.agentStates[ghostIndex]
        vector = Actions.directionToVector( action, 1 )
        ghostState.configuration = ghostState.configuration.generateSuccessor( vector )
    applyAction = staticmethod( applyAction )

    def decrementTimer( ghostState):
        timer = ghostState.scaredTimer
        if timer == 1:
            ghostState.configuration.pos = nearestPoint( ghostState.configuration.pos )
        ghostState.scaredTimer = max( 0, timer - 1 )
    decrementTimer = staticmethod( decrementTimer )

    def checkDeath( state, agentIndex):
        pacmanPosition = state.getPacmanPosition()
        if agentIndex == 0: # Pacman just moved; Anyone can kill him
            for index in range( 1, len( state.data.agentStates ) ):
                ghostState = state.data.agentStates[index]
                ghostPosition = ghostState.configuration.getPosition()
                if GhostRules.canKill( pacmanPosition, ghostPosition ):
                    GhostRules.collide( state, ghostState, index )
        else:
            ghostState = state.data.agentStates[agentIndex]
            ghostPosition = ghostState.configuration.getPosition()
            if GhostRules.canKill( pacmanPosition, ghostPosition ):
                GhostRules.collide( state, ghostState, agentIndex )
    checkDeath = staticmethod( checkDeath )

    def collide( state, ghostState, agentIndex):
        state.data.scoreChange += 200
        GhostRules.placeGhost(ghostState, agentIndex)
        # Added for first-person
        state.data._eaten[agentIndex] = True
        state.setGhostNotLiving(agentIndex)
    collide = staticmethod( collide )

    def canKill( pacmanPosition, ghostPosition ):
        return manhattanDistance( ghostPosition, pacmanPosition ) <= COLLISION_TOLERANCE
    canKill = staticmethod( canKill )

    def placeGhost(ghostState, agentIndex):
        pos = (agentIndex * 2 - 1, 1)
        direction = Directions.STOP
        ghostState.configuration = Configuration(pos, direction)
    placeGhost = staticmethod( placeGhost )

class RandomGhost:
    def __init__( self, index ):
        self.index = index

    def getAction( self, state ):
        return random.choice( state.getLegalActions( self.index ) )

    def getDistribution( self, state ):
        actions = state.getLegalActions( self.index )
        prob = 1.0 / len( actions )
        return [( prob, action ) for action in actions]

#############################
# FRAMEWORK TO START A GAME #
#############################

def default(str):
    return str + ' [Default: %default]'

def parseAgentArgs(str):
    if str == None: return {}
    pieces = str.split(',')
    opts = {}
    for p in pieces:
        if '=' in p:
            key, val = p.split('=')
        else:
            key,val = p, 1
        opts[key] = val
    return opts

def readCommand( argv ):
    """
    Processes the command used to run pacman from the command line.
    """
    from optparse import OptionParser
    usageStr = """
    USAGE:      python busters.py <options>
    EXAMPLE:    python busters.py --layout bigHunt
                  - starts an interactive game on a big board
    """
    parser = OptionParser(usageStr)

    parser.add_option('-n', '--numGames', dest='numGames', type='int',
                      help=default('the number of GAMES to play'), metavar='GAMES', default=1)
    parser.add_option('-l', '--layout', dest='layout',
                      help=default('the LAYOUT_FILE from which to load the map layout'),
                      metavar='LAYOUT_FILE', default='oneHunt')
    parser.add_option('-p', '--pacman', dest='pacman',
                      help=default('the agent TYPE in the pacmanAgents module to use'),
                      metavar='TYPE', default='BustersKeyboardAgent')
    parser.add_option('-a','--agentArgs',dest='agentArgs',
                      help='Comma seperated values sent to agent. e.g. "opt1=val1,opt2,opt3=val3"')
    parser.add_option('-g', '--ghosts', dest='ghost',
                      help=default('the ghost agent TYPE in the ghostAgents module to use'),
                      metavar = 'TYPE', default='RandomGhost')
    parser.add_option('-q', '--quietTextGraphics', action='store_true', dest='quietGraphics',
                      help='Generate minimal output and no graphics', default=False)
    parser.add_option('-k', '--numghosts', type='int', dest='numGhosts',
                      help=default('The maximum number of ghosts to use'), default=4)
    parser.add_option('-z', '--zoom', type='float', dest='zoom',
                      help=default('Zoom the size of the graphics window'), default=1.0)
    parser.add_option('-f', '--fixRandomSeed', action='store_true', dest='fixRandomSeed',
                      help='Fixes the random seed to always play the same game', default=False)
    parser.add_option('-s', '--showGhosts', action='store_true', dest='showGhosts',
                      help='Renders the ghosts in the display (cheating)', default=False)
    parser.add_option('-t', '--frameTime', dest='frameTime', type='float',
                      help=default('Time to delay between frames; <0 means keyboard'), default=0.1)

    options, otherjunk = parser.parse_args()
    if len(otherjunk) != 0:
        raise Exception('Command line input not understood: ' + otherjunk)
    args = dict()

    # Fix the random seed
    if options.fixRandomSeed: random.seed('bustersPacman')

    # Choose a layout
    args['layout'] = layout.getLayout( options.layout )
    if args['layout'] == None: raise Exception("The layout " + options.layout + " cannot be found")

    # Choose a ghost agent
    ghostType = loadAgent(options.ghost, options.quietGraphics)
    args['ghosts'] = [ghostType( i+1 ) for i in range( options.numGhosts )]

    # Choose a Pacman agent
    noKeyboard = options.quietGraphics
    pacmanType = loadAgent(options.pacman, noKeyboard)
    agentOpts = parseAgentArgs(options.agentArgs)
    agentOpts['ghostAgents'] = args['ghosts']
    pacman = pacmanType(**agentOpts) # Instantiate Pacman with agentArgs
    args['pacman'] = pacman

    import graphicsDisplay
    args['display'] = graphicsDisplay.FirstPersonPacmanGraphics(options.zoom, \
                                                                  options.showGhosts, \
                                                                  frameTime = options.frameTime)
    args['numGames'] = options.numGames

    return args

def loadAgent(pacman, nographics):
    # Looks through all pythonPath Directories for the right module,
    pythonPathStr = os.path.expandvars("$PYTHONPATH")
    if pythonPathStr.find(';') == -1:
        pythonPathDirs = pythonPathStr.split(':')
    else:
        pythonPathDirs = pythonPathStr.split(';')
    pythonPathDirs.append('.')

    for moduleDir in pythonPathDirs:
        if not os.path.isdir(moduleDir): continue
        moduleNames = [f for f in os.listdir(moduleDir) if f.endswith('gents.py')]
        for modulename in moduleNames:
            try:
                module = __import__(modulename[:-3])
            except ImportError:
                continue
            if pacman in dir(module):
                if nographics and modulename == 'keyboardAgents.py':
                    raise Exception('Using the keyboard requires graphics (not text display)')
                return getattr(module, pacman)
    raise Exception('The agent ' + pacman + ' is not specified in any *Agents.py.')

def runGames( layout, pacman, ghosts, display, numGames, maxMoves=-1):
    # Hack for agents writing to the display
    import __main__
    __main__.__dict__['_display'] = display

    rules = BustersGameRules()
    games = []

    for i in range( numGames ):
        game = rules.newGame( layout, pacman, ghosts, display, maxMoves )
        game.run()
        games.append(game)

    if numGames > 1:
        scores = [game.state.getScore() for game in games]
        wins = [game.state.isWin() for game in games]
        winRate = wins.count(True)/ float(len(wins))
        print 'Average Score:', sum(scores) / float(len(scores))
        print 'Scores:       ', ', '.join([str(score) for score in scores])
        print 'Win Rate:      %d/%d (%.2f)' % (wins.count(True), len(wins), winRate)
        print 'Record:       ', ', '.join([ ['Loss', 'Win'][int(w)] for w in wins])

    return games

if __name__ == '__main__':
    """
    The main function called when pacman.py is run
    from the command line:

    > python pacman.py

    See the usage string for more details.

    > python pacman.py --help
    """
    args = readCommand( sys.argv[1:] ) # Get game components based on input
    runGames( **args )
