using System.Runtime.InteropServices;


class Program
{
    [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
	public delegate int FilterFunc(Point p);

	[DllImport("E:\\wwwyssa_in_itmo\\yap\\seventhLab\\laba7\\my_lib.dll", CallingConvention = CallingConvention.Cdecl)]
	public static extern int filter([In, Out] Point[] arr, int n, FilterFunc func, [In, Out] Point[] output);

	[DllImport("E:\\wwwyssa_in_itmo\\yap\\seventhLab\\laba7\\my_lib.dll", CallingConvention = CallingConvention.Cdecl)]
	public static extern int q1(Point p);
	
	[DllImport("E:\\wwwyssa_in_itmo\\yap\\seventhLab\\laba7\\my_lib.dll", CallingConvention = CallingConvention.Cdecl)]
	public static extern int q2(Point p);
	
	[DllImport("E:\\wwwyssa_in_itmo\\yap\\seventhLab\\laba7\\my_lib.dll", CallingConvention = CallingConvention.Cdecl)]
	public static extern int q3(Point p);
	
	[DllImport("E:\\wwwyssa_in_itmo\\yap\\seventhLab\\laba7\\my_lib.dll", CallingConvention = CallingConvention.Cdecl)]
	public static extern int q4(Point p);
	static void Main(string[] args)
	{
        
		string filePath = "E:\\wwwyssa_in_itmo\\yap\\seventhLab\\laba7\\pairs.txt";
		var points = new List<Point>();
		foreach (var line in File.ReadLines(filePath))
		{
			var pairs = line.Split(' ');
			foreach (var pair in pairs)
			{
				var coords = pair.Split(',');
				if (int.TryParse(coords[0], out int x) && int.TryParse(coords[1], out int y))
				{
					points.Add(new Point { x = x, y = y });
				}
			}
		}

        Point[] pointsArray = points.ToArray();

		Point[] q1Filtered = new Point[pointsArray.Length];
		Point[] q2Filtered = new Point[pointsArray.Length];
		Point[] q3Filtered = new Point[pointsArray.Length];
		Point[] q4Filtered = new Point[pointsArray.Length];
		
		FilterFunc filterQ1 = q1;
		FilterFunc filterQ2 = q2;
		FilterFunc filterQ3 = q3;
		FilterFunc filterQ4 = q4;
		
		int q1Count = filter(pointsArray, pointsArray.Length, (p) => (p.x > 0 && p.y > 0 ? 1 : 0), q1Filtered);
		int q2Count = filter(pointsArray, pointsArray.Length, filterQ2, q2Filtered);
		int q3Count = filter(pointsArray, pointsArray.Length, filterQ3, q3Filtered);
		int q4Count = filter(pointsArray, pointsArray.Length, filterQ4, q4Filtered);
		
		Console.WriteLine($"\n1 chetvert: {q1Count} points");
		for (int i = 0; i < q1Count; i++)
		{
			Console.WriteLine($"({q1Filtered[i].x}, {q1Filtered[i].y})");
		}
		
		Console.WriteLine($"\n2 chetvert: {q2Count} points");
		for (int i = 0; i < q2Count; i++)
		{
			Console.WriteLine($"({q2Filtered[i].x}, {q2Filtered[i].y})");
		}
		
		Console.WriteLine($"\n3 chetvert: {q3Count} points");
		for (int i = 0; i < q3Count; i++)
		{
			Console.WriteLine($"({q3Filtered[i].x}, {q3Filtered[i].y})");
		}
		
		Console.WriteLine($"\n4 chetvert: {q4Count} points");
		for (int i = 0; i < q4Count; i++)
		{
			Console.WriteLine($"({q4Filtered[i].x}, {q4Filtered[i].y})");
		}
        
	}
    
}

