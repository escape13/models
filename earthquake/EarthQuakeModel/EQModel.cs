using System;

namespace EarthQuakeModel
{
    public class EQModel
    {
        /// <summary>
        /// Инициализирует модель землетрясений.
        /// </summary>
        /// <param name="Size">Размер квадратной решетки, в блоках.</param>
        /// <param name="CriticalStressLevel">Критический уровень напряженности, после которого происходит сдвиг блока.</param>
        /// <param name="StressGrowthValue">Размер приращения напряженности в каждом блоке.</param>
        /// <param name="EarthquakeSize">Размер землетрясения (кол-во толчков, произошедшиз единомоментно).</param>
        public EQModel(int Size, float CriticalStressLevel, float StressGrowthValue, int EarthquakeSize)
        {
            _plateSize = Size;
            _criticalStressLevel = CriticalStressLevel;
            _stressGrowthValue = StressGrowthValue;
            _time = 0;
            _eqSize = EarthquakeSize;
            _plate = initializePlate(_plateSize, _criticalStressLevel);
            _impact = new float[_plateSize, _plateSize];
            _eqNumber = 0;
        }

        /// <summary>
        /// Шаг времени при симуляции.
        /// </summary>
        public void DoStep()
        {
            //first step
            for (var _j = 0; _j < _plateSize; _j++)
                for (var _i = 0; _i < _plateSize; _i++)
                    _plate[_i, _j] += _stressGrowthValue;

            //step two 
            //phase one
            Array.Clear(_impact, 0, _impact.Length);
            var _currEq = 0;
            for (var _j = 0; _j < _plateSize; _j++)
                for (var _i = 0; _i < _plateSize; _i++)
                    if (FloatComparer.IsGreatherOrEqual(_criticalStressLevel, _plate[_i, _j]))
                    {
                        _currEq++;
                        //step three
                        earthQuake(ref _plate, ref _impact, _i, _j);
                    }

            //phase two
            for (var _j = 0; _j < _plateSize; _j++)
                for (var _i = 0; _i < _plateSize; _i++)
                    _plate[_i,_j] += _impact[_i, _j];

            
            if (_currEq >= _eqSize)
                _sizedEq++;
            _eqNumber += _currEq;
            _time++;
        }

        /// <summary>
        /// Доступ к сетке блоков. Только чтение.
        /// </summary>
        public float[,] Plate => (float[,])_plate.Clone();

        /// <summary>
        /// Размер сетки.
        /// </summary>
        public int Size => _plateSize;

        /// <summary>
        /// Шагов времени, прошедшего с начала симуляции.
        /// </summary>
        public int Time => _time;

        /// <summary>
        /// Критический уровень напряженности в блоке.
        /// </summary>
        public float CriticalStressLevel => _criticalStressLevel;
        
        /// <summary>
        /// Общее количество толчков (смещенных блоков) с начала симуляции.
        /// </summary>
        public int EarthquakesNumber => _eqNumber;

        /// <summary>
        /// Общее количество землетрясений заданного размера (т.е. таких, при которых количество толчков за один шаг симуляции превысило
        /// заданный размер.
        /// </summary>
        public int EarthquakesOfSetSizeNumber => _sizedEq;

        /// <summary>
        /// Инициализация сетки блоков, при которой каждому блоку задается начальное значение напряженности.
        /// Начальное значение не превышает 80% от критического уровня.
        /// </summary>
        /// <param name="size">Размер сетки в блоках.</param>
        /// <param name="criticalStressLevel">Критический уровень напряженности.</param>
        /// <returns></returns>
        private float[,] initializePlate(int size, float criticalStressLevel)
        {
            var _p = new float[size, size];

            var _r = new Random(DateTime.Now.Millisecond);

            for (var _j = 0; _j < size; _j++)
                for (var _i = 0; _i < size; _i++)
                {
                    var _rv = _r.NextDouble();
                    if (_rv < 0.7f)
                        _p[_i, _j] = (float)_r.NextDouble() * (criticalStressLevel * 0.5f);
                    else if (_rv < 0.9f)
                        _p[_i, _j] = (float)_r.NextDouble() * (criticalStressLevel * 0.7f);
                    else
                        _p[_i, _j] = (float)_r.NextDouble() * (criticalStressLevel * 0.8f);
                }
            return _p;
        }

        /// <summary>
        /// Расчет влияния толчка при нестабильной системе. Сброс напрженности фиксируется в отдельном массиве, чтобы не было наложений.
        /// </summary>
        /// <param name="plate"></param>
        /// <param name="impact"></param>
        /// <param name="i"></param>
        /// <param name="j"></param>
        private void earthQuake(ref float[,] plate, ref float[,] impact, int i, int j)
        {
            impact[i, j] -= _criticalStressLevel;
            var _impactShare = _criticalStressLevel / 4.0f;
            if (i > 0)
                impact[i - 1, j] += _impactShare;
            if (i < _plateSize - 1)
                impact[i + 1, j] += _impactShare;
            if (j > 0)
                impact[i, j - 1] += _impactShare;
            if (j < _plateSize - 1)
                impact[i, j + 1] += _impactShare;
        }

        private float[,] _plate;
        private float[,] _impact;
        private int _eqNumber;
        private int _plateSize;
        private float _criticalStressLevel;
        private float _stressGrowthValue;
        private int _time;
        private int _eqSize;
        private int _sizedEq;
    }
}
