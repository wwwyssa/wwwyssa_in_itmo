import pandas as pd
import matplotlib.pyplot as plt

file_path = 'SPFB.RTS-12.18_180901_181231.csv'
data = pd.read_csv(file_path)

data['<DATE>'] = pd.to_datetime(data['<DATE>'], format='%d.%m.%Y')

boxplot_data = []
labels = []

data = data.rename(columns={
    '<OPEN>': 'открытие',
    '<HIGH>': 'макс',
    '<LOW>': 'мин',
    '<CLOSE>': 'закрытие'
})

dates = ['24-09-2018', '24-10-2018', '26-11-2018', '3-12-2018']
for date in dates:
    for column in ['открытие', 'макс', 'мин', 'закрытие']:
        col = data[data['<DATE>'] == date][column]
        boxplot_data.append(col)
        labels.append(f"{column}\n{date}")

plt.figure(figsize=(16, 8))
plt.boxplot(boxplot_data, labels=labels)

plt.grid(axis='y', linestyle='--', alpha=0.5)
plt.tight_layout()
plt.savefig('boxplot.png')
plt.show()
