# Changes

## In PlayerController class
- Added another BoardState in PlayerController field to run better bot

## In Ship class
- Added getter methods in Ship class to help with AI
- Added a boolean field to see whether the ship is sunken or not

## BoardState modifications
- Added BoardState to the abstract class to reduce code duplication
- Removed constructor fields within board state to reduce coupling

## AI Modifications
- Improved AI to account for multiple cases to help sink ships faster
- Created a search heatmap to seek shots out if no information on hits is present
  to help with ai efficency

## Coord class
- Added methods to delegate controller features to coord do reduce code mixup
- Also utilized those methods to help with AI Modifications

