import os

# Directory containing the markdown files
directory = 'sexia/algebra/glava1/'

# Output file
output_file = 'sexia/algebra/glava1/combined.md'

# List of files to concatenate
files = [f'{i}.md' for i in range(1, 19)]

with open(output_file, 'w', encoding='utf-8') as outfile:
    for fname in files:
        with open(os.path.join(directory, fname), 'r', encoding='utf-8') as infile:
            outfile.write(infile.read())
            outfile.write('\n\n')  # Add a newline between files