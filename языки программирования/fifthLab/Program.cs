using System;
using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;

if (args.Length == 0)
{
    return;
}

var tasks = new List<Task<FileResult>>();

foreach (var path in args)
{
    tasks.Add(ProcessFileAsync(path));
}

var results = await Task.WhenAll(tasks);

foreach (var result in results)
{
    Console.WriteLine($"File: {result.Path}, Word Count: {result.WordCount}");
}
static async Task<FileResult> ProcessFileAsync(string path)
{
    try
    {
        var count = await CountWordsAsync(path);
        return new FileResult(path, count);
    }
    catch (Exception)
    {
        return new FileResult(path, 0);
    }
}

static async Task<long> CountWordsAsync(string path)
{
    var str = await File.ReadAllTextAsync(path).ConfigureAwait(false);
    var words = str.Split(new[] { ' ', '\n', '\r' }, StringSplitOptions.RemoveEmptyEntries);
    long count = words.Length;
    return count;
}

record FileResult(string Path, long WordCount);