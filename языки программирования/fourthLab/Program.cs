using Microsoft.VisualBasic;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json;

var csv = File.ReadAllLines(@"E:\wwwyssa_in_itmo\yap\fourthLab\tmdb_5000_credits.csv")
    .Skip(1)
    .Select(line =>
    {
        // "Name:"chdjhfjdhf,"
        line = Strings.Replace(line, ", ", ";");
        var splitLine = Strings.Split(line, ",").Select(x => Strings.Replace(x, ";", ", ")).ToList();
        return new Movie(splitLine[0], splitLine[1], splitLine[2], splitLine[3]);
    }).ToList();

Console.WriteLine($"Vsego filmov: {csv.Count}");

var directorCounts = csv
    .SelectMany(movie => movie.Crew
        .Where(crewMember => crewMember.Job == "Director")
        .Select(crewMember => crewMember.Name))
    .GroupBy(name => name)
    .ToList();

directorCounts.Sort((a, b) => b.Count() - a.Count());
Console.WriteLine($"Samiy krutoy rezisser: {directorCounts[0].Key} s {directorCounts[0].Count()} filmami.");

var workCount = csv
    .SelectMany(m => m.Crew)
    .Select(c => c.Job)
    .Distinct()
    .Count();

Console.WriteLine($"Kolichestvo professiy: {workCount}");


var outputLines = new List<string>();
outputLines.Add("movie_id,title,cast,crew");

static string JsonEscape(string? s)
{
    if (s is null) return "\"\"";
    return "\"" + s.Replace("\\", "\\\\").Replace("\"", "\\\"") + "\"";
}

string SerializeCast(List<Cast> list)
{
    var items = list.Select(c =>
        "{" +
        $"\"cast_id\":{JsonEscape(c.CastId)}," +
        $"\"character\":{JsonEscape(c.Character)}," +
        $"\"credit_id\":{JsonEscape(c.CreditId)}," +
        $"\"gender\":{JsonEscape(c.Gender)}," +
        $"\"id\":{JsonEscape(c.Id)}," +
        $"\"name\":{JsonEscape(c.Name)}," +
        $"\"order\":{JsonEscape(c.Order)}" +
        "}");
    return "[" + string.Join(", ", items) + "]";
}

string SerializeCrew(List<Crew> list)
{
    var items = list.Select(c =>
        "{" +
        $"\"credit_id\":{JsonEscape(c.CreditId)}," +
        $"\"department\":{JsonEscape(c.Department)}," +
        $"\"gender\":{JsonEscape(c.Gender)}," +
        $"\"id\":{JsonEscape(c.Id)}," +
        $"\"job\":{JsonEscape(c.Job)}," +
        $"\"name\":{JsonEscape(c.Name)}" +
        "}");
    return "[" + string.Join(", ", items) + "]";
}

static string EscapeCsvField(string field)
{
    if (string.IsNullOrEmpty(field)) return "";
    var f = field.Replace("\"", "\"\"");
    if (f.Contains(",") || f.Contains("\n") || f.Contains("\""))
        f = $"\"{f}\"";
    return f;
}

foreach (var m in csv)
{
    var castStr = SerializeCast(m.Cast);
    var crewStr = SerializeCrew(m.Crew);

    outputLines.Add(
        $"{EscapeCsvField(m.MovieId)},{EscapeCsvField(m.Title)},{EscapeCsvField(castStr)},{EscapeCsvField(crewStr)}"
    );
}

File.WriteAllLines(@"E:\wwwyssa_in_itmo\yap\fourthLab\result.csv", outputLines, System.Text.Encoding.UTF8);








public class Movie
{
    public string MovieId { get; set; }
    public string Title { get; set; }
    public List<Cast> Cast { get; set; }
    public List<Crew> Crew { get; set; }

    public Movie(string movieId, string title, string cast, string crew)
    {
        MovieId = movieId;
        Title = title;
        Cast = Strings.Replace(cast.TrimStart('"').TrimEnd('"'), "\"\"", "\"")
            .Split("}, {")
            .Select(x => new Cast(x))
            .ToList();
        Crew = Strings.Replace(crew.TrimStart('"').TrimEnd('"'), "\"\"", "\"")
            .Split("}, {")
            .Select(x => new Crew(x))
            .ToList();
    }

    public override string ToString()
    {
        return $"MovieId: {MovieId},\nTitle: {Title},\nCast: {string.Join(", ", Cast)},\nCrew: {string.Join(", ", Crew)}\n";
    }
}

public class Cast
{
    public string CastId { get; set; }
    public string Character { get; set; }
    public string CreditId { get; set; }
    public string Gender { get; set; }
    public string Id { get; set; }
    public string Name { get; set; }
    public string Order { get; set; }

    public Cast(string s)
    {
        try
        {
            var dict = Strings.Replace(s, "}]", "").Replace("[{", "")
            .Split(", \"")
            .Select(x => Strings.Split(x, ": "))
            .ToDictionary(x => Strings.Replace(x[0], "\"", ""), x => Strings.Replace(x[1], "\"", ""));
        CastId = dict.GetValueOrDefault("cast_id", "");
        Character = dict.GetValueOrDefault("character", "");
        CreditId = dict.GetValueOrDefault("credit_id", "");
        Gender = dict.GetValueOrDefault("gender", "");
        Id = dict.GetValueOrDefault("id", "");
        Name = dict.GetValueOrDefault("name", "");
        Order = dict.GetValueOrDefault("order", "");
        } catch (Exception ex)
        {
            //Console.WriteLine($"Error parsing cast member: {ex.Message}");
        }
        
    }

    public override string ToString()
    {
        return $"CastId: {CastId}, Character: {Character}, CreditId: {CreditId}, Gender: {Gender}, Id: {Id}, Name: {Name}, Order: {Order}";
    }

}
public class Crew
{
    public string CreditId { get; set; }
    public string Department { get; set; }
    public string Gender { get; set; }
    public string Id { get; set; }
    public string Job { get; set; }
    public string Name { get; set; }
    public Crew(string s)
    {
        try
        {
           var dict = Strings.Replace(s, "}]", "").Replace("[{", "")
            .Split(", \"")
            .Select(x => Strings.Split(x, ": "))
            .ToDictionary(x => Strings.Replace(x[0], "\"", ""), x => Strings.Replace(x[1], "\"", ""));
        CreditId = dict.GetValueOrDefault("credit_id", "");
        Department = dict.GetValueOrDefault("department", "");
        Gender = dict.GetValueOrDefault("gender", "");
        Id = dict.GetValueOrDefault("id", "");
        Job = dict.GetValueOrDefault("job", "");
        Name = dict.GetValueOrDefault("name", "");
        } catch (Exception ex)
        {
            //Console.WriteLine($"Error parsing crew member: {ex.Message}");
        }
        
    }
    public override string ToString()
    {
        return $"CreditId: {CreditId}, Department: {Department}, Gender: {Gender}, Id: {Id}, Job: {Job}, Name: {Name}";
    }
}
