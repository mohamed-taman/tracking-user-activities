#!/bin/bash
################################################################
# Script to test adding products,
# and monitoring the frequency within a specific window of time.
# Author: Mohamed Taman
# Version: 1.0
################################################################

# define product JSON message
prodJsonMsg='{"id":"prodId","name":"prodName","price":"prodPrice"}'

# Define the array of values
products=("iPhone" "Samsung Galaxy" "Nokia" "iPad")

# Get the length of the array
length=${#products[@]}

# Set the seed for random number generation
RANDOM=$$$(date +%s)

# Loop from 1 to 100
for ((i = 1; i <= 100; i++)); do

  # Generate a random index within the range of the array length
  index=$((RANDOM % length))

  # Access the value at the random index
  name=${products[index]}

  # Calculate price randomly
  price=$(awk "BEGIN {print $RANDOM / $i }")

  product=${prodJsonMsg/prodId/"$i"}
  product=${product/prodPrice/"$price"}
  product=${product/prodName/"$name"}

  # Print the selected product
  echo ""

  # Send the product to the server
  curl -s -X POST http://localhost:8080/api/products -H 'Content-Type: application/json' -d "$product"
done