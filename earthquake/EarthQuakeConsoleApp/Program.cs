using EarthQuakeModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace EarthQuakeConsoleApp
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.Title = "Программа симуляции землетрясений";
            Console.WriteLine("Программа симуляции землетрясений.");
            Console.WriteLine($"Версия {_version}. Copyright (C) {DateTime.Now.Year} by Nikolaev Vladimir.");
            Console.WriteLine();
            var _strSize = String.Empty;
            var _strCriticalStressValue = String.Empty;
            var _strStressGrowth = String.Empty;
            var _strEqSize = String.Empty;

            var _size = 0;
            var _criticalStressValue = 0.0f;
            var _stressGrowth = 0.0f;
            var _eqSize = 0;

            do
            {
                Console.Write("Введите размер плиты: ");
                _strSize = Console.ReadLine();
            } while (!Int32.TryParse(_strSize, out _size));

            do
            {
                Console.Write("Введите критический уровень напряженности в блоке: ");
                _strCriticalStressValue = Console.ReadLine();
            } while (!float.TryParse(_strCriticalStressValue, out _criticalStressValue));

            do
            {
                Console.Write("Введите приращение напряженности в блоке: ");
                _strStressGrowth = Console.ReadLine();
            } while (!float.TryParse(_strStressGrowth, out _stressGrowth));

            do
            {
                Console.Write("Введите размер землетрясений: ");
                _strEqSize = Console.ReadLine();
            } while (!Int32.TryParse(_strEqSize, out _eqSize));

            var _model = new EQModel(_size, _criticalStressValue, _stressGrowth, _eqSize);
            Console.WriteLine();

            Console.Clear();
            ShowModel(_model);

            do
            {
                while (!Console.KeyAvailable)
                {
                    Console.SetCursorPosition(0, 0);
                    _model.DoStep();
                    ShowModel(_model);
                    Thread.Sleep(250);
                }
            } while (Console.ReadKey(true).Key != ConsoleKey.Escape);

            Console.WriteLine();
            Console.WriteLine("Моделирование окончено.");
            Console.ReadKey();
        }

        private static void ShowModel(EQModel model)
        {
            Console.WriteLine($"Состояние модели в момент времени {model.Time}");
            Console.WriteLine("-----------------------------------------------------------------");
            var _currPlate = model.Plate;
            for (var _j = 0; _j < model.Size; _j++)
            {
                for (var _i = 0; _i < model.Size; _i++)
                {
                    Console.BackgroundColor = GetColor(_currPlate[_i, _j], model.CriticalStressLevel);
                    Console.ForegroundColor = ConsoleColor.Black;
                    Console.Write(" ");
                    Console.Write(_currPlate[_i, _j].ToString(_format));
                    Console.Write(" ");
                    Console.ResetColor();
                    //Console.Write(_delimeter);
                }
                Console.WriteLine();
            }
            Console.WriteLine("-----------------------------------------------------------------");
            Console.WriteLine($"Зафиксировано: толчков: {model.EarthquakesNumber} землетрясений заданного размера: {model.EarthquakesOfSetSizeNumber}");
        }

        private static ConsoleColor GetColor(float blockValue, float criticalStressLevel)
        {
            var _eq = ConsoleColor.DarkRed;
            var _hottest = ConsoleColor.Red;
            var _hot = ConsoleColor.Yellow;
            var _normal = ConsoleColor.Green;
            var _cold = ConsoleColor.Cyan;
            var _coldest = ConsoleColor.Blue;

            ConsoleColor _res;

            var _stressShare = criticalStressLevel / 5.0f;

            if (blockValue < _stressShare)
                _res = _coldest;
            else if (blockValue < _stressShare * 2.0f)
                _res = _cold;
            else if (blockValue < _stressShare * 3.0f)
                _res = _normal;
            else if (blockValue < _stressShare * 4.0f)
                _res = _hot;
            else if (blockValue < criticalStressLevel * 0.98f)
                _res = _hottest;
            else
                _res = _eq;

            return _res;
        }

        private static string _format = "F3";
        private static int _sizeOfPosition = 5; //4,001
        private static string _delimeter = "  ";

        private static string _version = "2.7";
    }
}
